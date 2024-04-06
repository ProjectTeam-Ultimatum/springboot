package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.plan.PlanDay;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planFoodId;

    private LocalDateTime planFoodDate;

    private LocalDateTime planFoodStayTime;

    private LocalDateTime planFoodArriveTime;

    private LocalDateTime planFoodRouteTime;

//    @OneToMany
//    @JoinColumn(name = "recommend_food_id")
//    private List<RecommendFood> recommendFoodId = new ArrayList<>();

    @ManyToOne
    private RecommendFood recommendFood;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDayId;

}
