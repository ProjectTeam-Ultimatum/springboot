package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.plan.PlanDay;

import java.time.LocalDate;
import java.util.Optional;

public interface PlanDayRepository extends JpaRepository<PlanDay, Long> {
    Optional<Object> findByPlanDayIdAndPlanDate(Long planDayId, LocalDate date);
}
