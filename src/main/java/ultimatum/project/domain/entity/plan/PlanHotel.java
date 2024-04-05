package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.plan.PlanDay;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

//    @OneToMany
//    @JoinColumn(name = "recommend_hotel_id")
//    private List<RecommendHotel> recommendHotelId = new ArrayList<>();

    @ManyToOne
    private RecommendHotel recommendHotel;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDayId;

}
