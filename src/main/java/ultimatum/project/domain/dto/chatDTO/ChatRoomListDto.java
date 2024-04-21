package ultimatum.project.domain.dto.chatDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListDto {
    private Long chatRoomId; // 채팅방 ID
    private String chatRoomName; //채팅방 이름
    private String chatRoomContent; //채팅방 내용
    private String creatorName;  // 작성자 이름
    private Long creatorAge;  // 작성자 나이
    private List<String> travelStyleTags; // 여행 스타일 태그 목록
}
