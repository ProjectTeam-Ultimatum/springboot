package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planDayId;

    private LocalDateTime planDayStartTime;

    private LocalDateTime planDayFinishTime;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan planId;
}
