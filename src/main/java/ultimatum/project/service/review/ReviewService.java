package ultimatum.project.service.review;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewImage;
import ultimatum.project.dto.reviewDTO.*;
import ultimatum.project.dto.reviewReplyDTO.ReadReplyResponse;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.ReviewImageRepository;
import ultimatum.project.repository.ReviewReplyRepository;
import ultimatum.project.repository.ReviewRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewReplyRepository replyRepository;
    private final AmazonS3 amazonS3;
    private final ReviewImageService imageService;
    private final String bucketName = "ultimatum0807"; // S3 버킷 이름 설정


    @Transactional
    public CreateReviewResponse createReview(CreateReviewRequest request, List<MultipartFile> files) {
        // Review 객체 생성 및 저장
        Review review = new Review();
        review.setReviewTitle(request.getReviewTitle());
        review.setReviewSubtitle(request.getReviewSubtitle());
        review.setReviewContent(request.getReviewContent());
        review.setReviewLocation(request.getReviewLocation());
        review = reviewRepository.save(review); // 저장하고 리턴받음으로써 ID를 획득


        //ReviewImage 객체 리스트를 생성하기 위한 준비
        List<ReviewImage> reviewImages = new ArrayList<>();
        // 이미지 파일 처리 로직
        for (MultipartFile file : files) {     // request.getReviewImages() 배열 또는 리스트를 가져옵니다.
            try {
                /**
                 * StringUtils.cleanPath : 파일경로에서 불필요한 문자나 경로 순회패턴('../')을 제거하여 파일 시스템 공격 방지에 도움
                 * Objects.requireNonNull : 파일의 이름이 null 인경우 예외 발생시킴
                 */
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                //s3에 파일 업로드
                // 파일의 저장위치와 파일 이름
                String s3Key = "uploads/" + originalFileName;
                // objectMetadata() : 업로드할 파일의 메타데이터를 설정하는데 사용(파일의 크기를 설정)
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                // amazonS3.putObject : 파일스트림과 메타데이터를 함께 S3에 업로드
                amazonS3.putObject(bucketName, s3Key, file.getInputStream(), metadata);

                //s3 파일 uri 생성
                /**
                 * amazonS3.getUrl().toString() : 성공적인 업로드 후 파일에 접근할 수 있는 uri를 얻기 위해
                 */
                String fileUri = amazonS3.getUrl(bucketName, s3Key).toString();


                // ReviewImage 객체 생성 및 저장
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setImageName(originalFileName);
                reviewImage.setImageUri(fileUri); //s3 파일 uri 설정
                reviewImage.setReview(review);
                reviewImages.add(reviewImage);

            } catch (IOException e) {

                throw new RuntimeException("이미지 저장 실패" + file.getOriginalFilename(), e);
            }
        }

        review.setReviewImages(reviewImages);
        reviewRepository.save(review);

        Review finalReview = review;
        reviewImages.forEach(image -> {
            image.setReview(finalReview);
            reviewImageRepository.save(image);
        });

        // CreateReviewResponse 객체 생성 및 반환
        return new CreateReviewResponse(
                review.getReviewId(),
                review.getReviewTitle(),
                review.getReviewSubtitle(),
                review.getReviewContent(),
                review.getReviewLike(),
                review.getReviewLocation(),
                reviewImages.stream().map(image ->
                        new ReviewImageResponse(
                                image.getReviewImageId(), image.getImageName(), image.getImageUri()
                        )
                ).collect(Collectors.toList())
        );

    }


    public Page<ReadAllReviewResponse> getAllReviews(Pageable pageable) {

        //페이지네이션을 적용하여 Review 엔티티들을 조회
        Page<Review> reviewPage = reviewRepository.findAll(pageable);

        //Review 엔티티들을 ReadReviewResponse DTO로 변환
        return reviewPage.map(review -> {
            //이미지중 하나씩만가져오기
            List<ReviewImageResponse> imageResponses = review.getReviewImages().stream()
                    .map(image -> new ReviewImageResponse(
                            image.getReviewImageId(), image.getImageName(), image.getImageUri()
                    )).collect(Collectors.toList());

            long replyCount = replyRepository.countByReview(review);
            List<ReadReplyResponse> replyResponses = review.getReviewReplies().stream()
                    .map(reply -> new ReadReplyResponse(
                            reply.getReviewReplyId(),
                            reply.getReviewReplyer(),
                            reply.getReviewReplyContent(),
                            reply.getReg_date(),
                            reply.getMod_date()
                    )).toList();

            //단일 이미지를 리스트에 넣음
            return new ReadAllReviewResponse(
                    review.getReviewId(),
                    review.getReviewTitle(),
                    review.getReviewSubtitle(),
                    review.getReviewContent(),
                    review.getReviewLike(),
                    review.getReviewLocation(),
                    imageResponses,
                    replyCount,
                    replyResponses,
                    review.getReg_date(),
                    review.getMod_date()
            );
        });
    }


    public ReadReviewByIdResponse getReviewById(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 게시글 ID를 찾을 수 없습니다." + reviewId));

        List<ReviewImageResponse> images = review.getReviewImages().stream()
                .map(image -> new ReviewImageResponse(
                        image.getReviewImageId(), image.getImageName(), image.getImageUri()
                )).collect(Collectors.toList());

        List<ReadReplyResponse> replies = review.getReviewReplies().stream()
                .map(reply -> new ReadReplyResponse(
                        reply.getReviewReplyId(), reply.getReviewReplyer(), reply.getReviewReplyContent(), reply.getReg_date(), reply.getMod_date()
                )).collect(Collectors.toList());

        return new ReadReviewByIdResponse(
                review.getReviewId(),
                review.getReviewTitle(),
                review.getReviewSubtitle(),
                review.getReviewContent(),
                review.getReviewLike(),
                review.getReviewLocation(),
                images,
                replies,
                review.getReg_date(),
                review.getMod_date()
        );

    }

    @Transactional
    public UpdateReviewResponse updateReview(Long reviewId, UpdateReviewRequest request
    ) throws CustomException {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        review.update(request.getReviewTitle(), request.getReviewSubtitle(), request.getReviewContent(), request.getReviewLocation());

        reviewRepository.save(review);

        imageService.updateImages(reviewId, request);


        List<ReviewImageResponse> imageResponses = review.getReviewImages().stream()
                .map(image -> new ReviewImageResponse(image.getReviewImageId(), image.getImageName(), image.getImageUri()))
                .collect(Collectors.toList());

        return new UpdateReviewResponse(
                review.getReviewId(),
                review.getReviewTitle(),
                review.getReviewSubtitle(),
                review.getReviewContent(),
                review.getReviewLocation(),
                imageResponses);
    }

    @Transactional
    public DeleteReviewResponse deleteReview(Long reviewId,List<String> deleteImages) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 게시글 ID를 찾을 수 없습니다." + reviewId));

        imageService.
                deleteImages(deleteImages);
        reviewRepository.delete(review);
        return new DeleteReviewResponse(reviewId);
    }


    @Transactional
    public ReviewLikeResponse updateReviewLike(Long reviewId, ReviewLikeRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰 아이디를 못찾음!"));
        review.setReviewLike(request.getReviewLike());
        reviewRepository.save(review);

        return new ReviewLikeResponse(
                review.getReviewId(),
                review.getReviewLike()
        );
    }
}