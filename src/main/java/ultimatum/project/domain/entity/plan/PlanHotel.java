package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planHotelId;

    @ManyToOne
    @JoinColumn(name = "recommend_hotel_id")
    private RecommendListHotel recommendListHotels;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDayId;

}
