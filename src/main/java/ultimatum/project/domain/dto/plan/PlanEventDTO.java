package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.plan.Plan;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanEventDTO {
    private LocalDateTime stayTime;
    private Long recommendEventId;
    private Plan planId;
}