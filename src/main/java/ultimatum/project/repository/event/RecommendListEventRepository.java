package ultimatum.project.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.event.RecommendListEvent;

public interface RecommendListEventRepository extends JpaRepository<RecommendListEvent, Long> {
    Page<RecommendListEvent> findByRecommendEventTagContainingIgnoreCase(String tag, Pageable pageable);
    Page<RecommendListEvent> findByRecommendEventRegionContainingIgnoreCase(String region, Pageable pageable);
    RecommendListEvent findByRecommendEventId(Long id); //ReplyService 축제행사 평점 등록
    Page<RecommendListEvent> findByRecommendEventTitleContainingIgnoreCase(String title, Pageable pageable);
}
