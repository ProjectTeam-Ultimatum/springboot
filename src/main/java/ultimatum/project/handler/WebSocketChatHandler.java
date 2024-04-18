package ultimatum.project.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ultimatum.project.service.chat.ChatService;
import ultimatum.project.service.chat.JwtTokenService;

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

    private final JwtTokenService jwtTokenService;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId : {session1, session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 사용자가 현재 어떤 채팅방에 있는지 관리하는 Map
    private final Map<WebSocketSession, Long> sessionChatRoomMap = new HashMap<>();


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
        if (member == null) {
            log.warn("Invalid token data for session id {}", session.getId());
            session.close();
            return;
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

        // 메시지 타입에 따른 처리
        switch (chatMessageDto.getMessageType()) {
            case ENTER:
                handleEnter(session, chatRoomId);
                String enterMessageContent = " 님이 입장하셨습니다.";
                ChatMessageDto enterMessage = ChatMessageDto.builder()
                        .messageType(MessageType.ENTER)
                        .chatRoomId(chatRoomId)
                        .senderId(chatMessageDto.getSenderId())
                        .message(enterMessageContent)
                        .build();
                sendMessageToChatRoom(enterMessage, chatRoomSessionMap.get(chatRoomId));
                break;
            case TALK:
                // TALK 메시지 처리 전 로그 추가
                log.info("Received TALK message: sessionId={}, senderId={}, message={}",
                        session.getId(), chatMessageDto.getSenderId(), chatMessageDto.getMessage());

                chatService.saveMessage(chatMessageDto, authentication);  // Modified to include authentication
                chatMessageDto.setMessage(chatMessageDto.getMessage());
                // TALK 메시지 브로드캐스트하기 전 로그 추가
                log.info("Broadcasting TALK message: sessionId={}, senderId={}, message={}",
                        session.getId(), chatMessageDto.getSenderId(), chatMessageDto.getMessage());

                sendMessageToChatRoom(chatMessageDto, chatRoomSessionMap.get(chatRoomId));
                log.info("Broadcasted TALK message: sessionId={}, senderId={}, message={}",
                        session.getId(), chatMessageDto.getSenderId(), chatMessageDto.getMessage());
                break;
            case IMAGE:
                if (chatMessageDto.getImageUrl() != null) {
                    sendMessageToChatRoom(chatMessageDto, chatRoomSessionMap.get(chatRoomId));
                }
                break;
            case LEAVE:
                handleLeave(session, chatRoomId);
                ChatMessageDto leaveMessage = ChatMessageDto.builder()
                        .messageType(MessageType.LEAVE)
                        .chatRoomId(chatRoomId)
                        .senderId(chatMessageDto.getSenderId())
                        .message(chatMessageDto.getSenderId() + "님이 퇴장하셨습니다.")
                        .build();
                sendMessageToChatRoom(leaveMessage, chatRoomSessionMap.get(chatRoomId));
                break;
            default:
                log.warn("Unhandled message type: {}", chatMessageDto.getMessageType());
                break;
        }
    }



    public void broadcastImageLink(String imageUrl, Long chatRoomId) {
        ChatMessageDto messageDto = new ChatMessageDto();
        messageDto.setImageUrl(imageUrl);
        messageDto.setMessageType(MessageType.IMAGE);
        Set<WebSocketSession> sessions = chatRoomSessionMap.get(chatRoomId);

        if (sessions != null) {
            sessions.forEach(session -> {
                try {
                    String messageJson = mapper.writeValueAsString(messageDto);
                    session.sendMessage(new TextMessage(messageJson));
                } catch (JsonProcessingException e) {
                    log.error("Failed to serialize chat message", e);
                } catch (IOException e) {
                    log.error("Failed to send message", e);
                }
            });
        } else {
            log.warn("No sessions found for chat room ID: {}", chatRoomId);
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

    private void handleEnter(WebSocketSession session, Long chatRoomId) {
        sessionChatRoomMap.put(session, chatRoomId);
        chatRoomSessionMap.get(chatRoomId).add(session);
    }


    private void handleLeave(WebSocketSession session, Long chatRoomId) {
        // 채팅방에서 세션 제거
        Set<WebSocketSession> chatRoomSessions = chatRoomSessionMap.getOrDefault(chatRoomId, new HashSet<>());
        chatRoomSessions.remove(session);

        // 세션과 채팅방 매핑 제거
        sessionChatRoomMap.remove(session);

        // SecurityContext에서 사용자 정보 가져오기
        SecurityContext context = (SecurityContext) session.getAttributes().get("SPRING_SECURITY_CONTEXT");
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof Member) {
                Member member = (Member) authentication.getPrincipal();

                // 퇴장 메시지 생성
                ChatMessageDto leaveMessage = ChatMessageDto.builder()
                        .messageType(MessageType.LEAVE)
                        .chatRoomId(chatRoomId)
                        .senderId(member.getMemberName())  // 사용자 이름으로 senderId 설정
                        .message(member.getMemberName() + "님이 퇴장하셨습니다.")
                        .build();

                // 채팅방의 모든 세션에 퇴장 메시지 방송
                sendMessageToChatRoom(leaveMessage, chatRoomSessions);
            }
        }

        // 채팅방 세션 목록 업데이트
        if (chatRoomSessions.isEmpty()) {
            chatRoomSessionMap.remove(chatRoomId);
        }
    }


    // ============채팅 관련 메소드==============

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    // 메시지를 생성하고 클라이언트에게 보내는 메서드
    // 메시지를 생성하고 클라이언트에게 보내는 메서드
    // 메시지를 생성하고 클라이언트에게 보내는 메서드
    public void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> sessions) {
        try {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    String messagePayload = mapper.writeValueAsString(chatMessageDto);
                    session.sendMessage(new TextMessage(messagePayload));
                }
            }
        } catch (Exception e) {
            log.error("Failed to send message", e);
        }
    }





    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("메시지 전송 실패", e);
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