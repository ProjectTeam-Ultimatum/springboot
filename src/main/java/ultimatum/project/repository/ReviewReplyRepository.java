package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewReply;

import java.util.List;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
    List<ReviewReply> findByReview(Review review);

    long countByReview(Review review);



}