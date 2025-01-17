package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.plan.Plan;
import ultimatum.project.domain.entity.place.RecommendListPlace;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanPlaceDTO {
    private LocalTime stayTime;
    private Long  recommendPlaceId;
    private Long  planId;
}