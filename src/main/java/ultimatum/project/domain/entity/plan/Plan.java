package ultimatum.project.domain.entity.plan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("plan")
    private List<PlanDay> planDays = new ArrayList<>();  // 여기서 리스트를 초기화

    // 빌더를 사용할 때 필요한 커스텀 로직
    public static class PlanBuilder {
        private List<PlanDay> planDays = new ArrayList<>();  // 빌더 내부에서도 명시적으로 초기화

        public PlanBuilder planDay(PlanDay planDay) {
            if (planDay != null) {
                planDay.setPlan(this.build());
                this.planDays.add(planDay);
            }
            return this;
        }
    }
}