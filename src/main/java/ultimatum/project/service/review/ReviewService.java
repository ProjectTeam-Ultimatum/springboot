package ultimatum.project.service.review;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.dto.reviewDTO.*;
import ultimatum.project.domain.dto.reviewReplyDTO.ReadReplyResponse;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewImage;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.member.MemberRepository;
import ultimatum.project.repository.image.ReviewImageRepository;
import ultimatum.project.repository.review.ReviewReplyRepository;
import ultimatum.project.repository.review.ReviewRepository;
import ultimatum.project.service.S3.S3Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewReplyRepository replyRepository;
    private final S3Service s3Service;
    private final ReviewImageService imageService;
    private final MemberRepository memberRepository;


    @Transactional
    public CreateReviewResponse createReview(Authentication authentication,
                                             CreateReviewRequest request,
                                             List<MultipartFile> files) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }
        String email = authentication.getName();
        Member member = memberRepository.findByMemberEmail(email);

        if (member == null) {
            throw new CustomException(ErrorCode.INVALID_MEMBER);
        }

        //  Review 객체 생성 및 저장
        Review review = new Review();
        review.setReviewTitle(request.getReviewTitle());
        review.setReviewSubtitle(request.getReviewSubtitle());
        review.setReviewContent(request.getReviewContent());
        review.setReviewLocation(request.getReviewLocation());
        review.setAuthor(email); //멤버 계정 이메일

        review = reviewRepository.save(review); // 저장하고 리턴받음으로써 ID를 획득


        // 이미지 파일 처리 및 ReviewImage 객체 리스트 생성
        List<ReviewImage> reviewImages = imageService.createReviewImages(files, review);
        if (reviewImages.isEmpty() && !files.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_PROCESSING_ERROR);
        }

        review.setReviewImages(reviewImages);
        reviewRepository.save(review);  // 변경된 review 객체를 다시 저장

        // 이미지 객체들을 DB에 저장
        reviewImageRepository.saveAll(reviewImages);


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
                                image.getReviewImageId(),
                                image.getImageName(),
                                image.getImageUri(),
                                image.getUuid()
                        )
                ).collect(Collectors.toList()),
                email //작성자 이메일 추가
        );

    }


    public Page<ReadAllReviewResponse> getAllReviews(String reviewLocation,
                                                     String keyword, Pageable pageable) {

        Page<Review> reviewPage;

        // 지역 정보만 사용하는 경우
        if (reviewLocation != null && !reviewLocation.isEmpty()) {
            reviewPage = reviewRepository.findAllByReviewLocation(reviewLocation, pageable);
        }
        // 검색 키워드만 사용하는 경우
        else if (keyword != null && !keyword.isEmpty()) {
            reviewPage = reviewRepository
                    .findByReviewTitleContainingIgnoreCaseOrReviewSubtitleContainingIgnoreCaseOrReviewContentContainingIgnoreCaseOrReviewLocationContainingIgnoreCase
                            (keyword, keyword, keyword, keyword, pageable);
        }
        // 파라미터가 없는 경우
        else {
            reviewPage = reviewRepository.findAll(pageable);
        }

        //Review 엔티티들을 ReadReviewResponse DTO로 변환
        return reviewPage.map(review -> {
            //이미지중 하나씩만가져오기
            List<ReviewImageResponse> imageResponses = review.getReviewImages().stream()
                    .map(image -> new ReviewImageResponse(
                            image.getReviewImageId(),
                            image.getImageName(),
                            image.getImageUri(),
                            image.getUuid()
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
                    review.getMod_date(),
                    review.getAuthor()
            );
        });
    }


    public ReadReviewByIdResponse getReviewById(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 게시글 ID를 찾을 수 없습니다." + reviewId));

        List<ReviewImageResponse> images = review.getReviewImages().stream()
                .map(image -> new ReviewImageResponse(
                        image.getReviewImageId(),
                        image.getImageName(),
                        image.getImageUri(),
                        image.getUuid()
                )).collect(Collectors.toList());

        List<ReadReplyResponse> replies = review.getReviewReplies().stream()
                .map(reply -> new ReadReplyResponse(
                        reply.getReviewReplyId(),
                        reply.getReviewReplyer(),
                        reply.getReviewReplyContent(),
                        reply.getReg_date(),
                        reply.getMod_date()
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
                review.getMod_date(),
                review.getAuthor()
        );

    }

    @Transactional
    public UpdateReviewResponse updateReview(Authentication authentication,
                                             Long reviewId,
                                             UpdateReviewRequest request) throws CustomException {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }
        String email = authentication.getName();
        Member member = memberRepository.findByMemberEmail(email);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!member.getMemberEmail().equals(review.getAuthor())) {
            throw new CustomException(ErrorCode.REVIEW_NOT_YOURS);
        }

        review.update(request.getReviewTitle(), request.getReviewSubtitle(), request.getReviewContent(), request.getReviewLocation());

        reviewRepository.save(review);
        imageService.updateReviewImages(reviewId, request);

        // uuid로 저장되어있는 리뷰 이미지를 Response 객체로 변환하여 목록 생성
        List<ReviewImageResponse> imageResponses = review.getReviewImages().stream()
                .map(image -> new ReviewImageResponse(image.getReviewImageId(), image.getImageName(), image.getImageUri(), image.getUuid()))
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
    public DeleteReviewResponse deleteReview(Authentication authentication, Long reviewId) {


        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }
        String email = authentication.getName();
        Member member = memberRepository.findByMemberEmail(email);


        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!member.getMemberEmail().equals(review.getAuthor())) {
            throw new CustomException(ErrorCode.REVIEW_NOT_YOURS);
        }

        // S3에서 연관된 이미지 모두 삭제
        review.getReviewImages().forEach(image -> {
            try {
                s3Service.deleteFileFromS3(image.getImageUri());
            } catch (Exception e) {
                // 예외를 잡아서 트랜잭션 롤백 유도
                throw new RuntimeException("Failed to delete image from S3: " + image.getImageUri(), e);
            }
        });
        // 리뷰 삭제
        reviewRepository.delete(review);
        return new DeleteReviewResponse(reviewId);
    }


    @Transactional
    public ReviewLikeResponse updateReviewLike(Long reviewId, ReviewLikeRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        review.setReviewLike(request.getReviewLike());
        reviewRepository.save(review);

        return new ReviewLikeResponse(
                review.getReviewId(),
                review.getReviewLike()
        );
    }


}