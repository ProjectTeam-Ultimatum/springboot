package ultimatum.project.chatting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    private Long chatRoomId; // 채팅방 ID
    private String chatRoomName; //채팅방 이름
}
