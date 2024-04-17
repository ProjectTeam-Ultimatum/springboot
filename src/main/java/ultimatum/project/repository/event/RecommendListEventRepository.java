package ultimatum.project.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;

public interface RecommendListEventRepository extends JpaRepository<RecommendListEvent, Long> {
    Page<RecommendListEvent> findByRecommendEventTagContainingIgnoreCase(String tag, Pageable pageable);
    Page<RecommendListEvent> findByRecommendEventRegionContainingIgnoreCase(String region, Pageable pageable);
}
