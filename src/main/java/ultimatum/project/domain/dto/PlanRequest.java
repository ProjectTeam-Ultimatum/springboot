package ultimatum.project.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ultimatum.project.domain.dto.plan.PlanEventDTO;
import ultimatum.project.domain.dto.plan.PlanFoodDTO;
import ultimatum.project.domain.dto.plan.PlanHotelDTO;
import ultimatum.project.domain.dto.plan.PlanPlaceDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class PlanRequest {
    private String memberId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate planStartDay;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate planEndDay;
    private String planTitle;
    private List<PlanDayRequest> planDays;
    private List<PlanFoodDTO> planFoods;
    private List<PlanEventDTO> planEvents;
    private List<PlanPlaceDTO> planPlaces;
    private List<PlanHotelDTO> planHotels;

    @Getter
    @Setter
    public static class PlanDayRequest {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        @JsonFormat(pattern = "HH:mm:ss")
        private LocalTime startTime;
        @JsonFormat(pattern = "HH:mm:ss")
        private LocalTime finishTime;
    }
    @Override
    public String toString() {
        return "PlanRequest{" +
                "memberId='" + memberId + '\'' +
                ", planStartDay=" + planStartDay +
                ", planFinishDay=" + planEndDay +
                ", planTitle='" + planTitle + '\'' +
                '}';
    }
}
