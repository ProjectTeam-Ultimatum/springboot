package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.plan.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailDTO {
    private LocalTime stayTime;
    private Long recommendFoodId;  // 음식점 추천 ID
    private Long recommendEventId; // 이벤트 추천 ID
    private Long recommendPlaceId; // 장소 추천 ID
    private Long recommendHotelId;
    private Long planDayId;
    private Long planId;


}
