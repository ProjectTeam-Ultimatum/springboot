package ultimatum.project.domain.entity.place;

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
public class PlanPlace {

    @Id
    @GeneratedValue
    private Long planPlaceId;

    private LocalDateTime planPlaceDate;

    private LocalDateTime planPlaceStayTime;

    private LocalDateTime planPlaceArriveTime;

    @ManyToOne
    private RecommendPlace recommendFoodId;

    @ManyToOne
    private PlanDay planDayId;


}
