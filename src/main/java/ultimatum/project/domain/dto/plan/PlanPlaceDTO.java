package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.plan.Plan;
import ultimatum.project.domain.entity.place.RecommendListPlace;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanPlaceDTO {
    private LocalDateTime stayTime;
    private RecommendListPlace recommendPlaceId;
    private Plan planId;
}