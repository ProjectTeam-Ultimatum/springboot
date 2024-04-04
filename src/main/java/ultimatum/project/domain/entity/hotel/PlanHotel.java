package ultimatum.project.domain.entity.hotel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ultimatum.project.domain.entity.plan.PlanDay;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanHotel {
    @Id
    @GeneratedValue
    private Long planHotelId;

    private LocalDateTime planHotelDate;

    private LocalDateTime planHotelStayTime;

    private LocalDateTime planHotelArriveTime;

    private LocalDateTime planHotelRouteTime;

    @ManyToOne
    private RecommendHotel recommendHotelId;

    @ManyToOne
    private PlanDay planDayId;

}
