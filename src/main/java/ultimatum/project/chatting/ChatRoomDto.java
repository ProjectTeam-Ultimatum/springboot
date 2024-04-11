package ultimatum.project.chatting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    private Long chatRoomId; // 채팅방 ID
    private String chatRoomName; //채팅방 이름
    private String chatRoomContent; //채팅방 내용
    private List<String> travelStyleTags; // 여행 스타일 태그 목록
}
