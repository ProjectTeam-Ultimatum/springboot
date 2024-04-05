package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.dto.reviewDTO.*;
import ultimatum.project.service.review.ReviewService;

import java.util.List;

@Tag(name = "reviews", description = "사용자 게시판 api")
@Controller
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
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
    public ResponseEntity<List<ReadReviewResponse>> getAllReviews() {
        List<ReadReviewResponse> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReadReviewResponse> getReviewById(@PathVariable Long reviewId) {
        ReadReviewResponse response = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value="/{reviewId}", consumes = {"multipart/form-data"})
    public ResponseEntity<UpdateReviewResponse> updateReview(@PathVariable Long reviewId,
                                                             @RequestParam("reviewTitle") String reviewTitle,
                                                             @RequestParam("reviewSubtitle") String reviewSubtitle,
                                                             @RequestParam("reviewContent") String reviewContent,
                                                             @RequestParam("reviewLocation") String reviewLocation,
                                                             @RequestParam("images") List<MultipartFile> images) {
        UpdateReviewRequest request = new UpdateReviewRequest(
                reviewTitle, reviewSubtitle, reviewContent, reviewLocation, images);


        UpdateReviewResponse response = reviewService.updateReview(reviewId, request, images);

        return new ResponseEntity<>(response, HttpStatus.UPGRADE_REQUIRED);

    }
}