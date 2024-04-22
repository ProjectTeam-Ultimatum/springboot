package ultimatum.project.repository.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.plan.PlanPlace;

public interface PlanPlaceRepository extends JpaRepository<PlanPlace, Long> {
}
