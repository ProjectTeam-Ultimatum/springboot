package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    private String memberId;

    private LocalDate planStartDay;

    private LocalDate planEndDay;

    private String planTitle;

    public Plan(Long planId) {
        this.planId = planId;
    }
}
