package ultimatum.project.chatting;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;


    @ManyToOne
    private ChatRoom chatRoom; // 메시지가 속한 채팅방

    private Long senderId; // 메시지를 보낸 사용자의 ID

    private String message; //메시지 내용

    @Enumerated(EnumType.STRING)
    private MessageType messageType; // 메시지 타입 추가

    public enum MessageType {
        ENTER, TALK, LEAVE
    }
}
