package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.plan.PlanDay;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDayDTO {
    private Long planDayId;
    private LocalTime startTime;
    private LocalTime finishTime;
    private LocalDate planDate;

    public PlanDayDTO(PlanDay planDay) {
        this.planDate = planDay.getPlanDate();
        this.startTime = planDay.getPlanDayStartTime();
        this.finishTime = planDay.getPlanDayFinishTime();
    }

    // Getters and Setters
    public Long getPlanDayId() {
        return planDayId;
    }

    public void setPlanDayId(Long planDayId) {
        this.planDayId = planDayId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDate planDate) {
        this.planDate = planDate;
    }
}