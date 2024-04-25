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
public class PlanPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planPlaceId;

    private LocalTime planPlaceStayTime;

    @ManyToOne
    @JoinColumn(name = "recommend_place_id")
    private RecommendListPlace recommendListPlaces;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

}
