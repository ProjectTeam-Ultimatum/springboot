package ultimatum.project.chatting;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    private MessageType messageType; // 메시지 타입
    private Long chatRoomId; // 채팅방 번호
    private Long senderId; // 채팅을 보낸 사용자의 ID
    private String message; // 메시지 내용
}
