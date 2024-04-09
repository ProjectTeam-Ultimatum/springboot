package ultimatum.project.service.review;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewReply;
import ultimatum.project.dto.reviewReplyDTO.*;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.ReviewReplyRepository;
import ultimatum.project.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Log4j2
@RequiredArgsConstructor
public class ReviewReplyService {

    private final ReviewReplyRepository reviewReplyRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public CreateReplyResponse createReply(Long reviewId,CreateReplyRequest request) {


        //리뷰 엔티티 조회
       Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

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

    public List<ReadReplyResponse> getAllReplyById(Long review_id){

        Review review = reviewRepository.findById(review_id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        List<ReviewReply> replies = reviewReplyRepository.findByReview(review);

        //조회된 ReviewReply 목록을 ReadReplyResponseDTO 목록 변환
        return replies.stream().map(reply -> new ReadReplyResponse(
                reply.getReviewReplyId(),
                reply.getReviewReplyer(),
                reply.getReviewReplyContent(),
                reply.getReg_date(),
                reply.getMod_date()
        )).collect(Collectors.toList());
    }


    @Transactional
    public UpdateReplyResponse updateReply(Long reply_id, UpdateReplyRequest request){

        ReviewReply reviewReply = reviewReplyRepository.findById(reply_id)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        reviewReply.update(request.getReviewReplyer(), request.getReviewReplyContent());

        reviewReplyRepository.save(reviewReply);

        return new UpdateReplyResponse(
                reviewReply.getReviewReplyId(),
                reviewReply.getReviewReplyer(),
                reviewReply.getReviewReplyContent()
        );
    }


    @Transactional
    public DeleteReplyResponse deleteReply (Long reply_id){

        ReviewReply reviewReply = reviewReplyRepository.findById(reply_id)
                .orElseThrow(()-> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        reviewReplyRepository.delete(reviewReply);
        return new DeleteReplyResponse(reply_id);
    }
}
