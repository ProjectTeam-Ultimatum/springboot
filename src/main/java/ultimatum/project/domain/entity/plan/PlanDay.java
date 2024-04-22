package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    private LocalTime planDayStartTime;

    private LocalTime planDayFinishTime;

    private LocalDate planDate;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan planId;

    public void setPlan(Plan plan) {
        this.planId = plan;
    }
}
