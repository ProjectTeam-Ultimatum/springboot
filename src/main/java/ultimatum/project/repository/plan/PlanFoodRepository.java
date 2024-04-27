package ultimatum.project.repository.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ultimatum.project.domain.entity.plan.PlanFood;

public interface PlanFoodRepository extends JpaRepository<PlanFood, Long> {
    // Plan ID와 RecommendFood ID를 기준으로 PlanFood가 존재하는지 확인
    @Query("SELECT COUNT(pf) > 0 FROM PlanFood pf WHERE pf.plan.planId = :planId AND pf.recommendFood.recommendFoodId = :foodId")
    boolean existsByPlanAndRecommendFoodId(Long planId, Long foodId);
}