package ultimatum.project.domain.entity.plan;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanDay {

    @Id
    @GeneratedValue
    private Long planDayId;

    private LocalDateTime planDayStartTime;

    private LocalDateTime planDayFinishTime;

    @ManyToOne
    private Plan planId;
}
