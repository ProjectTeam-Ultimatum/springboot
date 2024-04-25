package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;


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
    private RecommendListHotel recommendHotel;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDay;

}
