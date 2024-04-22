package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.plan.Plan;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanFoodDTO {
    private LocalTime stayTime;
    private RecommendListFood recommendFoodId;
    private Plan planId;
}