package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.plan.PlanDay;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlanDayRepository extends JpaRepository<PlanDay, Long> {
    // Plan 엔티티의 planId를 기준으로 PlanDay 객체를 찾는 메서드
    List<PlanDay> findByPlan_PlanId(Long planId);

    // Plan ID와 날짜를 기준으로 PlanDay 객체를 찾는 메서드
    Optional<PlanDay> findByPlan_PlanIdAndPlanDate(Long planId, LocalDate date);

    // 마지막 PlanDay를 조회하는 메서드
    default PlanDay findLastByPlan_PlanId(Long planId) {
        List<PlanDay> days = findByPlan_PlanId(planId);
        return days.isEmpty() ? null : days.get(days.size() - 1);
    }
}