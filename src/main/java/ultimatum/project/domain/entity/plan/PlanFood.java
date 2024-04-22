package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;

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

    private LocalDateTime planFoodStayTime;

    @ManyToOne
    @JoinColumn(name = "recommend_food_id")
    private RecommendListFood recommendListFoods;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan planId;

}
