package ultimatum.project.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.reviewReplyDTO.*;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewReply;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Transactional
    public CreateReplyResponse createReply(Authentication authentication, Long reviewId, CreateReplyRequest request) {

        if(authentication == null || !authentication.isAuthenticated()){
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }
        String email = authentication.getName();
        Member member = memberRepository.findByMemberEmail(email);

        //리뷰 엔티티 조회
       Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

       if( member == null) {
           throw new CustomException(ErrorCode.INVALID_MEMBER);
       }

        //DTO를 엔티티로 변환 후 저장
        ReviewReply reviewReply = new ReviewReply();
        reviewReply.setReview(review);
        reviewReply.setReviewReplyer(email);
        reviewReply.setReviewReplyContent(request.getReviewReplyContent());
        reviewReply = reviewReplyRepository.save(reviewReply);

        log.info(reviewReply);
        //저장된 엔티티를 다시 DTO로 변환하여 반환
        return new CreateReplyResponse(
                reviewReply.getReviewReplyId(),
                email,
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
    public UpdateReplyResponse updateReply(Authentication authentication, Long reply_id, UpdateReplyRequest request){

        if (authentication == null || !authentication.isAuthenticated())  {
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }
        String email = authentication.getName();
        Member member = memberRepository.findByMemberEmail(email);

        ReviewReply reviewReply = reviewReplyRepository.findById(reply_id)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!member.getMemberEmail().equals(reviewReply.getReviewReplyer())) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_YOURS);
        }
        reviewReply.update(request.getReviewReplyContent());

        reviewReplyRepository.save(reviewReply);

        return new UpdateReplyResponse(
                reviewReply.getReviewReplyId(),
                reviewReply.getReviewReplyer(),
                reviewReply.getReviewReplyContent()
        );
    }


    @Transactional
    public DeleteReplyResponse deleteReply (Authentication authentication, Long reply_id){

        if(authentication == null || !authentication.isAuthenticated()){
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }

        String email = authentication.getName();
        Member member = memberRepository.findByMemberEmail(email);

        ReviewReply reviewReply = reviewReplyRepository.findById(reply_id)
                .orElseThrow(()-> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if(!member.getMemberEmail().equals(reviewReply.getReviewReplyer())){
            throw new CustomException(ErrorCode.RESOURCE_NOT_YOURS);
        }

        reviewReplyRepository.delete(reviewReply);
        return new DeleteReplyResponse(reply_id);
    }
}
