package ultimatum.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.chatDTO.ChatMessageDto;
import ultimatum.project.domain.dto.chatDTO.ChatRoomDto;
import ultimatum.project.domain.dto.chatDTO.ChatRoomListDto;
import ultimatum.project.domain.entity.chat.ChatRoom;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.global.config.Security.auth.PrincipalDetails;
import ultimatum.project.service.chat.ChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;

//===================================================채팅방 컨트롤러 =====================================================

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody ChatRoomDto chatRoomDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Member member = principalDetails.getUser();  // PrincipalDetails에서 Member 객체를 가져옵니다.

            ChatRoom createdRoom = chatService.createChatRoom(chatRoomDto, member);
            ChatRoomDto savedRoomDto = new ChatRoomDto(
                    createdRoom.getChatRoomId(),
                    createdRoom.getMember().getMemberId(),
                    createdRoom.getChatRoomName(),
                    chatRoomDto.getCreatorGender(),
                    createdRoom.getChatRoomContent(),
                    createdRoom.getTravelStyleTags(),
                    createdRoom.getReviewLocation()
            );

            return ResponseEntity.ok(savedRoomDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomListDto>> getChatRooms() {
        List<ChatRoomListDto> chatRooms = chatService.getChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    // 로그인한 사용자의 채팅방 목록 조회

    // ChatController.java
    @GetMapping("/connected-rooms")
    public ResponseEntity<List<ChatRoomListDto>> getConnectedChatRooms(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getUser();
        List<ChatRoomListDto> connectedRooms = chatService.getConnectedChatRooms(member.getMemberId());
        return ResponseEntity.ok(connectedRooms);
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
