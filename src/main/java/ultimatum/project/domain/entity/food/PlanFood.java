package ultimatum.project.domain.entity.food;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ultimatum.project.domain.entity.plan.PlanDay;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanFood {

    @Id
    @GeneratedValue
    private Long planFoodId;

    private LocalDateTime planFoodDate;

    private LocalDateTime planFoodStayTime;

    private LocalDateTime planFoodArriveTime;

    private LocalDateTime planFoodRouteTime;

    @ManyToOne
    private RecommendFood recommendFoodId;

    @ManyToOne
    private PlanDay planDayId;



}
