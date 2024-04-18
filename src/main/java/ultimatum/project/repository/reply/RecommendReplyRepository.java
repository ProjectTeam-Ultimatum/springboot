package ultimatum.project.repository.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;

public interface RecommendReplyRepository extends JpaRepository<RecommendReply, Long> {
}
