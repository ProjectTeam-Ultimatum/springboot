package ultimatum.project.repository.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ultimatum.project.domain.entity.food.RecommendFood;

public interface RecommendFoodRepository extends JpaRepository<RecommendFood, Long> {
    // 특정 Plan과 RecommendFood ID에 해당하는 PlanFood가 존재하는지 확인
    @Query("SELECT count(pf) > 0 FROM PlanFood pf WHERE pf.plan.planId = :planId AND pf.recommendFood.id = :foodId")
    boolean existsByPlanIdAndFoodId(Long planId, Long foodId);
}