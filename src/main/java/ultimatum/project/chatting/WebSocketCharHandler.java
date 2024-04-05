package ultimatum.project.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketCharHandler extends TextWebSocketHandler {
    private  final ObjectMapper mapper;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId : {session1, session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 사용자가 현재 어떤 채팅방에 있는지 관리하는 Map
    private final Map<WebSocketSession, Long> sessionChatRoomMap = new HashMap<>();

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        // TODO Auto-generated method stub
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);

        Long chatRoomId = chatMessageDto.getChatRoomId();
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }

        switch (chatMessageDto.getMessageType()) {
            case ENTER:
                handleEnter(session, chatRoomId);
                break;
            case LEAVE:
                handleLeave(session, chatRoomId);
                break;
            // 다른 메시지 타입 처리
        }

        sendMessageToChatRoom(chatMessageDto, chatRoomSessionMap.get(chatRoomId));
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
        chatRoomSessionMap.getOrDefault(chatRoomId, new HashSet<>()).remove(session);
        sessionChatRoomMap.remove(session);
    }

    // ============채팅 관련 메소드==============

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.forEach(sess -> sendMessage(sess, chatMessageDto));
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