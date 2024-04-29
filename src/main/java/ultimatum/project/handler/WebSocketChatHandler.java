package ultimatum.project.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ultimatum.project.domain.dto.chatDTO.ChatMessageDto;
import ultimatum.project.domain.dto.chatDTO.MessageType;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.service.chat.ChatRoomSessionService;
import ultimatum.project.service.chat.ChatService;
import ultimatum.project.service.chat.JwtTokenService;
import ultimatum.project.service.chat.ReportService;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private  final ObjectMapper mapper;

    private final ChatService chatService;

    private final ReportService reportService;

    private final JwtTokenService jwtTokenService;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId : {session1, session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 사용자가 현재 어떤 채팅방에 있는지 관리하는 Map
    private final Map<WebSocketSession, Long> sessionChatRoomMap = new HashMap<>();

    @Autowired
    private ChatRoomSessionService chatRoomSessionService;


    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String query = session.getUri().getQuery();
        Map<String, String> queryParams = parseQuery(query);
        String token = queryParams.get("token");

        // 'Bearer ' 접두사 제거
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || !jwtTokenService.validateToken(token)) {
            log.warn("Invalid or missing token for session id {}", session.getId());
            session.close();
            return;
        }

        Member member = jwtTokenService.parseToken(token);
        if (member != null) {
            // 사용자 ID를 세션 속성에 저장
            session.getAttributes().put("email", member.getMemberId());
        }

        log.info("{} 연결됨", session.getId());
        sessions.add(session);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        context.setAuthentication(authentication);
        session.getAttributes().put("SPRING_SECURITY_CONTEXT", context);
    }

    // 유틸리티 메소드: 쿼리 파라미터 파싱
    private Map<String, String> parseQuery(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                queryParams.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
                        URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
            }
        }
        return queryParams;
    }


    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);

        SecurityContext context = (SecurityContext) session.getAttributes().get("SPRING_SECURITY_CONTEXT");

        String senderEmail = ((Member) context.getAuthentication().getPrincipal()).getMemberEmail();

        // SecurityContext와 Authentication 검증
        if (context == null || context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
            log.warn("No authentication information available for session id {}", session.getId());
            return; // 인증 정보 없이는 처리하지 않음
        }

        Authentication authentication = context.getAuthentication();
        Long chatRoomId = chatMessageDto.getChatRoomId();
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }

        // 메시지 타입에 따른 처리ㅎ
        switch (chatMessageDto.getMessageType()) {
            case ENTER:
                chatRoomSessionService.addSessionToRoom(chatRoomId, session);
                handleEnter(session, chatRoomId, authentication);
                break;
            case TALK:
                // TALK 메시지 처리 전 로그 추가
                //log.info("Received TALK message: sessionId={}, senderId={}, message={}",
                        //session.getId(), chatMessageDto.getSenderId(), chatMessageDto.getMessage());
                chatService.saveMessage(chatMessageDto, authentication);  // Modified to include authentication
                log.info("chatMessageDto {}!!!!!!!!!!!!", chatMessageDto.getSenderEmail());
                chatRoomSessionService.getSessionsForRoom(chatRoomId)
                        .forEach(s -> sendMessageToSession(chatMessageDto, s));
                break;
            case IMAGE:
                if (chatMessageDto.getImageUrl() != null) {
                    sendMessageToChatRoom(chatMessageDto, chatRoomSessionMap.get(chatRoomId));
                }
                break;
            case LEAVE:
                handleLeave(session, chatRoomId);
                break;
            default:
                log.warn("Unhandled message type: {}", chatMessageDto.getMessageType());
                break;
        }
    }



    // 소켓 종료 확인
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        Long chatRoomId = sessionChatRoomMap.get(session);
        if (chatRoomId != null) {
            handleLeave(session, chatRoomId);
        }
        sessions.remove(session);
    }

    private void handleEnter(WebSocketSession session, Long chatRoomId, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        String username = member.getMemberName();
        String email = member.getMemberEmail();

        // 입장 메시지 생성 및 전송
        String enterMessageContent = username + " 님이 입장하셨습니다.";
        ChatMessageDto enterMessage = ChatMessageDto.builder()
                .messageType(MessageType.ENTER)
                .chatRoomId(chatRoomId)
                .senderId(username)
                .message(enterMessageContent)
                .build();

        // 세션을 채팅방에 추가
        chatRoomSessionService.addSessionToRoom(chatRoomId, session);

        // 채팅방에 있는 모든 세션에게 입장 메시지 전송
        chatRoomSessionService.getSessionsForRoom(chatRoomId)
                .forEach(s -> sendMessageToSession(enterMessage, s));

        log.info("User {} entered chat room {} with session ID: {}", username, chatRoomId, session.getId());

        log.info("email{}",email);
        // 사용자의 신고 횟수 확인
        int reportCount = reportService.countUserReports(email);
        log.info("reportCount{}", reportCount);
        if (reportCount >= 3) {
            // 경고 메시지 전송
            ChatMessageDto warningMessage = ChatMessageDto.builder()
                    .messageType(MessageType.WARNING)
                    .chatRoomId(chatRoomId)
                    .senderId(username)
                    .message("경고 ❗: " + username + " 님은 최근 3개월동안 3회이상 신고 접수된 사용자입니다. 피해 위험이 있습니다 주의하세요!")
                    .build();
            chatRoomSessionService.addSessionToRoom(chatRoomId, session);
            chatRoomSessionService.getSessionsForRoom(chatRoomId)
                    .forEach(s -> sendMessageToSession(warningMessage, s));
        }
    }




    private void handleLeave(WebSocketSession session, Long chatRoomId) {
        chatRoomSessionService.removeSessionFromRoom(chatRoomId, session);
        log.info("Session {} removed from chat room {}", session.getId(), chatRoomId);
    }


    private void sendMessageToSession(ChatMessageDto message, WebSocketSession session) {
        try {
            if (session.isOpen()) {
                String messagePayload = new ObjectMapper().writeValueAsString(message);
                session.sendMessage(new TextMessage(messagePayload));
                log.info("Message sent to session ID: {}", session.getId());
            }
        } catch (IOException e) {
            log.error("Failed to send message to session ID: {}", session.getId(), e);
        }
    }


    // ============채팅 관련 메소드==============


    // 메시지를 생성하고 클라이언트에게 보내는 메서드
    public void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> sessions) {
        try {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    String messagePayload = mapper.writeValueAsString(chatMessageDto);
                    session.sendMessage(new TextMessage(messagePayload));
                    log.info("Sent message to session ID: {}", session.getId());
                }
            }
        } catch (Exception e) {
            log.error("Failed to send message", e);
        }
    }

    // 채팅방 전체메시지 메소드
    public void broadcastMessageToAllUsers(ChatMessageDto message) {
        // 모든 채팅방의 세션을 반복하여 메시지 전송
        for (Map.Entry<Long, Set<WebSocketSession>> entry : chatRoomSessionMap.entrySet()) {
            for (WebSocketSession session : entry.getValue()) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(message)));
                    } catch (IOException e) {
                        // 오류 처리 로직
                        System.err.println("Failed to send message to " + session.getId());
                    }
                }
            }
        }
    }
    //  ==========================================
}


/**
 * sessions : 현재 연결된 웹소켓 세션들을 담는 Set
 * - 메모리 상에 현재 연결된 웹소켓을 담고, 세션이 추가되거나 종료되면 반영한다.
 * - afterConnectionEstablished(), afterConnectionClosed() 에서 반영
 * chatRoomSessionMap : 채팅방 당 연결된 세션을 담은 Map, Map<roomId, Session Set> 의 형태로 세션을 저장한다.
 * - 채팅 메세지를 보낼 채팅방을 찾고, 해당 채팅방에 속한 세션들에게 메세지를 전송한다.
 * handleTextMessage() : 웹소켓 통신 시 메세지 전송을 다루는 부분
 * - textWebSocketHandler 클래스의 handleTextMessage() 메소드를 Override 하여 구현
 * - 웹소켓 통신 메세지를 TextMessage 로 받고, 이를 mapper 로 파싱하여 ChatMessageDto 로 변환
 */