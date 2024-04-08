package ultimatum.project.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper; // ModelMapper 라이브러리를 사용한 예시

    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
    private final ObjectMapper mapper;

    // 채팅 메시지 저장
    public void saveMessage(ChatMessageDto chatMessageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSenderId(chatMessageDto.getSenderId());
        chatMessage.setMessage(chatMessageDto.getMessage());
        chatMessage.setMessageType(chatMessageDto.getMessageType());

        messageRepository.save(chatMessage);
    }


    // 모든 채팅방 조회
    public List<ChatRoomDto> getChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(chatRoom -> new ChatRoomDto(chatRoom.getChatRoomId(), chatRoom.getChatRoomName()))
                .collect(Collectors.toList());
    }

    // 특정 채팅방에 입장
    public ChatRoomDto enterChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .map(chatRoom -> new ChatRoomDto(chatRoom.getChatRoomId(), chatRoom.getChatRoomName()))
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다!"));
    }

    // 채팅 메시지 전송
    public void sendMessage(ChatMessageDto chatMessageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다!"));

        ChatMessage message = new ChatMessage();
        message.setChatRoom(chatRoom);
        message.setSenderId(chatMessageDto.getSenderId());
        message.setMessage(chatMessageDto.getMessage());

        messageRepository.save(message);

        // 채팅방 참여자에게 새 메시지 알림
    }

    //채팅방 생성
    public ChatRoomDto createChatRoom(ChatRoomDto chatRoomDto) {
        // ChatRoom 엔티티를 생성하고, chatRoomDto에서 받은 정보를 설정
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomName(chatRoomDto.getChatRoomName());
        // chatRoom 엔티티를 저장
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        // 저장된 chatRoom 엔티티를 기반으로 ChatRoomDto 생성 및 반환
        return new ChatRoomDto(savedRoom.getChatRoomId(), savedRoom.getChatRoomName());
    }

    //채팅방 삭제
    public boolean deleteChatRoom(Long roomId) {
        if(chatRoomRepository.existsById(roomId)) {
            chatRoomRepository.deleteById(roomId);
            return true;
        } else {
            return false;
        }
    }

    public void leaveChatRoom(ChatMessageDto chatMessageDto) {
        // 채팅방 ID와 사용자 ID를 기반으로 해당 채팅방에서 사용자 세션 제거
        Long chatRoomId = chatMessageDto.getChatRoomId();
        Long senderId = chatMessageDto.getSenderId();

        // 사용자의 웹소켓 세션 찾기 (이 부분은 웹소켓 세션 관리 방식에 따라 달라질 수 있습니다)
        // 예제에서는 간단히 모든 세션을 순회하며 사용자 ID가 일치하는 세션을 찾아낸 후 해당 세션을 제거합니다
        chatRoomSessionMap.getOrDefault(chatRoomId, new HashSet<>()).removeIf(session -> {
            // 세션에 저장된 사용자 ID를 조회하는 방법은 애플리케이션 구현에 따라 달라질 수 있습니다
            // 여기서는 예시를 위한 가정으로 처리합니다
            Long storedSenderId = findSenderIdBySession(session);
            return senderId.equals(storedSenderId);
        });

        // 채팅방에서 사용자를 제거한 후 필요한 후처리
        // 예를 들어, 채팅방 내의 다른 사용자들에게 사용자 퇴장 알림 메시지를 전송할 수 있습니다
        ChatMessageDto leaveMessageDto = new ChatMessageDto();
        leaveMessageDto.setChatRoomId(chatRoomId);
        leaveMessageDto.setMessage(senderId + " 사용자가 채팅방을 떠났습니다.");
        // 채팅방의 모든 사용자에게 퇴장 알림 메시지 전송
        sendMessageToChatRoom(leaveMessageDto, chatRoomSessionMap.get(chatRoomId));
    }

    private Long findSenderIdBySession(WebSocketSession session) {
        // 세션의 속성에서 사용자 ID 조회
        return (Long) session.getAttributes().get("userId");
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        // ChatMessageDto 객체를 JSON 문자열로 변환
        String messagePayload;
        try {
            messagePayload = mapper.writeValueAsString(chatMessageDto);
        } catch (IOException e) {
            log.error("메시지 변환 중 오류 발생", e);
            return;
        }

        // 채팅방의 모든 세션에 메시지 전송
        for (WebSocketSession session : chatRoomSession) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(messagePayload));
                }
            } catch (IOException e) {
                log.error("메시지 전송 중 오류 발생", e);
            }
        }
    }

    //메세지 내용 불러오기
    public List<ChatMessageDto> getMessagesByChatRoomId(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        List<ChatMessage> messages = messageRepository.findByChatRoom(chatRoom);

        return messages.stream()
                .map(message -> new ChatMessageDto(
                        message.getMessageType(),
                        message.getChatRoom().getChatRoomId(),
                        message.getSenderId(),
                        message.getMessage()))
                .collect(Collectors.toList());
    }


}
