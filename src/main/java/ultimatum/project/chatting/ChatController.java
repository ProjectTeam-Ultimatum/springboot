package ultimatum.project.chatting;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

//===================================================채팅방 컨트롤러 =====================================================

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {

        // chatService를 사용하여 채팅방 생성
        ChatRoomDto createdRoom = chatService.createChatRoom(chatRoomDto);
        return ResponseEntity.ok(createdRoom);
    }


    // 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getChatRooms() {
        // chatService를 사용하여 채팅방 목록 조회
        return ResponseEntity.ok().body(chatService.getChatRooms());
    }

    // 특정 채팅방 입장
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> enterChatRoom(@PathVariable Long roomId) {
        // roomId에 해당하는 채팅방 정보 조회 및 반환
        return ResponseEntity.ok().body(chatService.enterChatRoom(roomId));
    }

    // 채팅방 삭제
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable Long roomId) {
        boolean result = chatService.deleteChatRoom(roomId);
        if(result) {
            return ResponseEntity.ok().build(); // 성공적으로 삭제되었을 때
        } else {
            return ResponseEntity.notFound().build(); // 채팅방을 찾을 수 없거나 삭제에 실패했을 때
        }
    }

    // 채팅방 퇴장 처리
    @PostMapping("/room/{chatRoomId}/leave")
    public ResponseEntity<?> leaveChatRoom(@PathVariable Long chatRoomId, @RequestBody ChatMessageDto chatMessageDto) {
        chatMessageDto.setChatRoomId(chatRoomId); // URL에서 받은 chatRoomId를 chatMessageDto에 설정
        chatService.leaveChatRoom(chatMessageDto);
        return ResponseEntity.ok().build();
    }
//===========================================메세지 컨트롤러==============================================================
    // 채팅 메시지 전송
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDto chatMessageDto) {
        // chatService를 사용하여 메시지 전송
        chatService.sendMessage(chatMessageDto);
        return ResponseEntity.ok().build();
    }

    // 채팅 내역
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByChatRoomId(@PathVariable Long roomId) {
        List<ChatMessageDto> messages = chatService.getMessagesByChatRoomId(roomId);
        return ResponseEntity.ok(messages);
    }


}
