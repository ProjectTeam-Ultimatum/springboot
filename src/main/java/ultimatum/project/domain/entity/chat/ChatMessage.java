package ultimatum.project.domain.entity.chat;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.dto.chatDTO.MessageType;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoom; // 메시지가 속한 채팅방

    private String senderId; // 메시지를 보낸 사용자의 ID

    private String message; // 메시지 내용

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private MessageType messageType; // 메시지 타입
}
