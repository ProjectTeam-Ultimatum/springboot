package ultimatum.project.domain.entity.plan;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue
    private Long planId;

//    뭔지 모르겠음
//    @OneToMany
//    private

    private Long memberId;

    private LocalDateTime travelPeriod;

    private String planTitle;

}
