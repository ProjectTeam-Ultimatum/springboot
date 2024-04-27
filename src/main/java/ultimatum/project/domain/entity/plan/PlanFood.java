package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
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
public class PlanFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planFoodId;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDay;

    @ManyToOne
    @JoinColumn(name = "recommend_food_id")
    private RecommendListFood recommendFood;

    private LocalTime planFoodStayTime;

    // 생성자
    public PlanFood(Plan plan, PlanDay planDay, RecommendListFood recommendFood, LocalTime planFoodStayTime) {
        this.plan = plan;
        this.planDay = planDay;
        this.recommendFood = recommendFood;
        this.planFoodStayTime = planFoodStayTime;
    }

}