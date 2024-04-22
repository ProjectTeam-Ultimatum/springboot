package ultimatum.project.repository.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.plan.PlanFood;

public interface PlanFoodRepository extends JpaRepository<PlanFood, Long> {
}
