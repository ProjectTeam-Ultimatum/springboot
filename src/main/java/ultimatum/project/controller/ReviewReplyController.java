package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.reviewReplyDTO.*;
import ultimatum.project.service.review.ReviewReplyService;

import java.util.List;

@Log4j2
@Tag(name = "reviewReply", description = "사용자 게시판 댓글 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewReplyController {
    private final ReviewReplyService reviewReplyService;


    @PostMapping(value = "/{review_id}/reply")
    @Operation(summary = "댓글 생성")
    public ResponseEntity<CreateReplyResponse> createReply(Authentication authentication,
                                                           @PathVariable Long review_id,
                                                           @RequestBody CreateReplyRequest request) {
        //댓글 생성 요청 처리
        CreateReplyResponse response = reviewReplyService.createReply(authentication, review_id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{review_id}/replies")
    @Operation(summary = "해당 게시물에 작성된 댓글 보기")
    public ResponseEntity<List<ReadReplyResponse>> readReplyByReview(@PathVariable Long review_id) {
        List<ReadReplyResponse> response = reviewReplyService.getAllReplyById(review_id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reply_id}/reply")
    @Operation(summary = "댓글수정")
    public ResponseEntity<UpdateReplyResponse> updateReplyByReplyId(Authentication authentication,
                                                                    @PathVariable Long reply_id,
                                                                    @RequestBody UpdateReplyRequest request) {

        UpdateReplyResponse response = reviewReplyService.updateReply(authentication, reply_id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reply_id}/reply")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<DeleteReplyResponse> deleteReply(Authentication authentication,
                                                           @PathVariable Long reply_id) {
        reviewReplyService.deleteReply(authentication, reply_id);
        return ResponseEntity.ok(new DeleteReplyResponse(reply_id));
    }
}
