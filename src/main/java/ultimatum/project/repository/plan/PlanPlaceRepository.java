package ultimatum.project.repository.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ultimatum.project.domain.entity.plan.PlanPlace;

public interface PlanPlaceRepository extends JpaRepository<PlanPlace, Long> {

    @Query("SELECT COUNT(e) > 0 FROM PlanPlace e WHERE e.plan.planId = :planId AND e.recommendPlace.recommendPlaceId = :placeId")
    boolean existsByPlanIdAndPlaceId(Long planId, Long placeId);
}
