package ultimatum.project.repository.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.plan.PlanEvent;

public interface PlanEventRepository extends JpaRepository<PlanEvent, Long> {
}
