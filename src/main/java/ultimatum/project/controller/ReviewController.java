package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.dto.reviewDTO.*;
import ultimatum.project.service.review.ReviewService;

import java.util.List;

@Tag(name = "reviews", description = "사용자 게시판 api")
@Controller
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "게시글 작성", description = "여러가지입력해")
    public ResponseEntity<CreateReviewResponse> reviewCreate(@RequestParam("reviewTitle") String reviewTitle,
                                                             @RequestParam("reviewSubtitle") String reviewSubtitle,
                                                             @RequestParam("reviewContent") String reviewContent,
                                                             @RequestParam("reviewLocation") String reviewLocation,
                                                             @RequestParam("reviewImages") List<MultipartFile> reviewImages) {
        // CreateReviewRequest 객체를 생성하고 파라미터로 전달받은 값을 설정합니다.
        CreateReviewRequest request = new CreateReviewRequest(
                reviewTitle, reviewSubtitle, reviewContent, reviewLocation, reviewImages);

        CreateReviewResponse response = reviewService.createReview(request, reviewImages);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "게시글 전체 보기")
    public ResponseEntity<Page<ReadReviewResponse>> getAllReviews(
            @PageableDefault (size = 6, sort = "reviewId", direction = Sort.Direction.DESC)Pageable pageable) {
        Page<ReadReviewResponse> reviews = reviewService.getAllReviews(pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{review_id}")
    @Operation(summary = "게시글 하나만 보기")

    public ResponseEntity<ReadReviewResponse> getReviewById(@PathVariable Long review_id) {
        ReadReviewResponse response = reviewService.getReviewById(review_id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value="/{review_id}", consumes = {"multipart/form-data"})
    @Operation(summary = "게시글 수정")

    public ResponseEntity<UpdateReviewResponse> updateReview(@PathVariable Long review_id,
                                                             @RequestParam("reviewTitle") String reviewTitle,
                                                             @RequestParam("reviewSubtitle") String reviewSubtitle,
                                                             @RequestParam("reviewContent") String reviewContent,
                                                             @RequestParam("reviewLocation") String reviewLocation,
                                                             @RequestParam("images") List<MultipartFile> images) {
        UpdateReviewRequest request = new UpdateReviewRequest(
                reviewTitle, reviewSubtitle, reviewContent, reviewLocation, null );
                                                                                    //이미지 관련 정보는 여기서 처리하지 않음.



        UpdateReviewResponse response = reviewService.updateReview(review_id, request, images);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping( "/{review_id}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<DeleteReviewResponse> deleteReview(@PathVariable Long review_id){
       reviewService.deleteReview(review_id);

        return ResponseEntity.ok(new DeleteReviewResponse(review_id));
    }
}