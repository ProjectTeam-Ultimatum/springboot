package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.review.ReviewReply;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
}