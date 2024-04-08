package ultimatum.project.chatting;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    private String chatRoomName;

    // ChatMessage 엔터티와 OneToMany 관계 설정
    // cascade = CascadeType.ALL로 설정하여 ChatRoom 삭제 시 관련 ChatMessage도 함께 삭제되도록 함
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;

}
