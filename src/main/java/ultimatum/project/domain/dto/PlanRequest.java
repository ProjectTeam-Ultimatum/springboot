package ultimatum.project.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Getter
    @Setter
    public static class PlanDayRequest {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startTime;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endTime;
    }
    @Override
    public String toString() {
        return "PlanRequest{" +
                "memberId='" + memberId + '\'' +
                ", planStartDay=" + planStartDay +
                ", planEndDay=" + planEndDay +
                ", planTitle='" + planTitle + '\'' +
                '}';
    }
}
