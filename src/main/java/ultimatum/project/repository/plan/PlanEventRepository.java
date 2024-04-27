package ultimatum.project.repository.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ultimatum.project.domain.entity.plan.PlanEvent;

public interface PlanEventRepository extends JpaRepository<PlanEvent, Long> {

    @Query("SELECT COUNT(e) > 0 FROM PlanEvent e WHERE e.plan.planId = :planId AND e.recommendEvent.recommendEventId = :eventId")
    boolean existsByPlanIdAndEventId(Long planId, Long eventId);
}