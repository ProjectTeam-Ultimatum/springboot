package ultimatum.project.chatting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    // ChatRoom 엔티티를 사용하여 해당 채팅방의 모든 메시지를 조회
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}
