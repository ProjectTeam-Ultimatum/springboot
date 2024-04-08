package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.dto.reviewReplyDTO.CreateReplyRequest;
import ultimatum.project.dto.reviewReplyDTO.CreateReplyResponse;
import ultimatum.project.service.review.ReviewReplyService;

@Log4j2
@Tag(name = "reviewReply", description = "사용자 게시판 댓글 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:8080")
public class ReviewReplyController {
    private final ReviewReplyService reviewReplyService;

    @PostMapping(value = "/{review_id}/replies")
    @Operation(summary = "댓글 생성")
    public ResponseEntity<CreateReplyResponse> createReply(@PathVariable Long review_id,
                                                           @RequestBody CreateReplyRequest request){
        //댓글 생성 요청 처리
        CreateReplyResponse response = reviewReplyService.createReply(review_id,request);
        return ResponseEntity.ok(response);
    }
}
