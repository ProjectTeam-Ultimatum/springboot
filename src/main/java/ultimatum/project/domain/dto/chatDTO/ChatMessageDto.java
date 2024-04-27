package ultimatum.project.domain.dto.chatDTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private Long chatMessageId;
    private MessageType messageType; // 메시지 타입
    private Long chatRoomId; // 채팅방 번호
    private String senderId; // 채팅을 보낸 사용자의 ID
    private String senderEmail; // 메시지를 보낸 사용자의 EMAIL
    private String message; // 메시지 내용
    private String imageUrl;
}
