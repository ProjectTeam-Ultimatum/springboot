package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planPlaceId;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDay;

    @ManyToOne
    @JoinColumn(name = "recommend_place_id")
    private RecommendListPlace recommendPlace;

    private LocalTime planPlaceStayTime;

    // 생성자
    public PlanPlace(Plan plan, PlanDay planDay, RecommendListPlace recommendPlace, LocalTime planPlaceStayTime) {
        this.plan = plan;
        this.planDay = planDay;
        this.recommendPlace = recommendPlace;
        this.planPlaceStayTime = planPlaceStayTime;
    }
}