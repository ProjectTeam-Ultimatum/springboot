package ultimatum.project.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.event.RecommendListEvent;

public interface RecommendListEventRepository extends JpaRepository<RecommendListEvent, Long> {
}
