package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.plan.PlanDay;

public interface PlanDayRepository extends JpaRepository<PlanDay, Long> {
}
