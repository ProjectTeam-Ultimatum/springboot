package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.plan.Plan;
import ultimatum.project.domain.entity.plan.PlanEvent;
import ultimatum.project.domain.entity.plan.PlanFood;
import ultimatum.project.domain.entity.plan.PlanPlace;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailDTO {
    private LocalDateTime stayTime;
    private Long recommendId;
    private Long planId;

    public PlanFood toPlanFood(RecommendListFood recommendFoodId) {
        return PlanFood.builder()
                .planId(new Plan(planId))  // Assuming Plan class has a constructor accepting planId
                .recommendListFoods(recommendFoodId)
                .planFoodStayTime(stayTime)
                .build();
    }

    public PlanEvent toPlanEvent(RecommendListEvent recommendEventId) {
        return PlanEvent.builder()
                .planId(new Plan(planId))
                .recommendListEvents(recommendEventId)
                .planEventStayTime(stayTime)
                .build();
    }

    public PlanPlace toPlanPlace(RecommendListPlace recommendPlaceId) {
        return PlanPlace.builder()
                .planId(new Plan(planId))
                .recommendListPlaces(recommendPlaceId)
                .planPlaceStayTime(stayTime)
                .build();
    }
}
