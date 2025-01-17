package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.dto.reviewDTO.*;
import ultimatum.project.service.review.ReviewService;

import java.util.List;

@Log4j2
@Tag(name = "reviews", description = "사용자 게시판 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "게시글 작성", description = "여러가지입력해")
    public ResponseEntity<CreateReviewResponse> reviewCreate(Authentication authentication,
                                                             @RequestParam("reviewTitle") String reviewTitle,
                                                             @RequestParam("reviewSubtitle") String reviewSubtitle,
                                                             @RequestParam("reviewContent") String reviewContent,
                                                             @RequestParam("reviewLocation") String reviewLocation,
                                                             @RequestParam("reviewImages") List<MultipartFile> reviewImages) {

        // CreateReviewRequest 객체를 생성하고 파라미터로 전달받은 값을 설정합니다.
        CreateReviewRequest request = new CreateReviewRequest(
                reviewTitle, reviewSubtitle, reviewContent, reviewLocation, reviewImages);
        // 서비스를 호출하여 리뷰를 생성하고 응답 객체를 반환합니다.
        CreateReviewResponse response = reviewService.createReview(authentication, request, reviewImages);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "게시글 전체 보기")
    public ResponseEntity<Page<ReadAllReviewResponse>> getAllReviews(
            @RequestParam(value = "reviewLocation", required = false) String reviewLocation,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 6, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReadAllReviewResponse> reviews = reviewService.getAllReviews(reviewLocation, keyword, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{review_id}")
    @Operation(summary = "게시글 하나만 보기")

    public ResponseEntity<ReadReviewByIdResponse> getReviewById(@PathVariable Long review_id) {
        ReadReviewByIdResponse response = reviewService.getReviewById(review_id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{review_id}", consumes = {"multipart/form-data"})
    @Operation(summary = "게시글 수정")

    public ResponseEntity<UpdateReviewResponse> updateReview(Authentication authentication,
                                                             @PathVariable Long review_id,
                                                             @RequestParam("reviewTitle") String reviewTitle,
                                                             @RequestParam("reviewSubtitle") String reviewSubtitle,
                                                             @RequestParam("reviewContent") String reviewContent,
                                                             @RequestParam("reviewLocation") String reviewLocation,
                                                             @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
                                                             @RequestParam(value = "deleteImages", required = false) List<String> deleteImages) {

        UpdateReviewRequest request = new UpdateReviewRequest(
                reviewTitle, reviewSubtitle, reviewContent, reviewLocation, newImages, deleteImages);
        //이미지 관련 정보는 여기서 처리하지 않음.
        UpdateReviewResponse response = reviewService.updateReview(authentication, review_id, request);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{review_id}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<DeleteReviewResponse> deleteReview(Authentication authentication,
                                                             @PathVariable Long review_id) {
        reviewService.deleteReview(authentication, review_id);

        return ResponseEntity.ok(new DeleteReviewResponse(review_id));
    }

    @PostMapping("/{review_id}")
    @Operation(summary = "좋아요 수정")
    public ResponseEntity<ReviewLikeResponse> updateLike(@PathVariable Long review_id, @RequestBody ReviewLikeRequest request) {

        // 리뷰 서비스에서 좋아요 수를 업데이트하는 메소드를 호출합니다.
        ReviewLikeResponse response = reviewService.updateReviewLike(review_id, request);
        return ResponseEntity.ok(response);
    }
}