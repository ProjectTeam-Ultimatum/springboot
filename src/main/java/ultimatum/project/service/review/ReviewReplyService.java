package ultimatum.project.service.review;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewReply;
import ultimatum.project.dto.reviewReplyDTO.CreateReplyRequest;
import ultimatum.project.dto.reviewReplyDTO.CreateReplyResponse;
import ultimatum.project.repository.ReviewReplyRepository;
import ultimatum.project.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
@Log4j2
@RequiredArgsConstructor
public class ReviewReplyService {

    private final ReviewReplyRepository reviewReplyRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public CreateReplyResponse createReply(Long reviewId,CreateReplyRequest request){


            //리뷰 엔티티 조회
           Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을수 없습니다." + reviewId));

            //DTO를 엔티티로 변환 후 저장
            ReviewReply reviewReply = new ReviewReply();
            reviewReply.setReview(review);
            reviewReply.setReviewReplyer(request.getReviewReplyer());
            reviewReply.setReviewReplyContent(request.getReviewReplyContent());
            reviewReply = reviewReplyRepository.save(reviewReply);

            log.info(reviewReply);
            //저장된 엔티티를 다시 DTO로 변환하여 반환
            return new CreateReplyResponse(
                    reviewReply.getReviewReplyId(),
                    reviewReply.getReviewReplyer(),
                    reviewReply.getReviewReplyContent());

    }

}
