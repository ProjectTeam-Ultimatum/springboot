package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.plan.Plan;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponseDTO {
    private Long planId;
    private List<PlanDayDTO> planDays;
    private String planTitle;


    public PlanResponseDTO(Plan plan) {
        this.planId = plan.getPlanId();
        this.planDays = plan.getPlanDays().stream()
                .map(PlanDayDTO::new)
                .collect(Collectors.toList());
        this.planTitle = plan.getPlanTitle();
    }
    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public List<PlanDayDTO> getPlanDays() {
        return planDays;
    }

    public void setPlanDays(List<PlanDayDTO> planDays) {
        this.planDays = planDays;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }
}
