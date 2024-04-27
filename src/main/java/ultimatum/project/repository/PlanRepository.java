package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.plan.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
