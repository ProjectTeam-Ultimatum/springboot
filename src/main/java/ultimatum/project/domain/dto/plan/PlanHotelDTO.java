package ultimatum.project.domain.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.plan.PlanDay;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanHotelDTO {
    private Long recommendHotelId;
    private Long planDayId;
}
