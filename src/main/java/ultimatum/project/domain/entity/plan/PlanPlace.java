package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.place.RecommendPlace;
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
public class PlanPlace {

    @Id
    @GeneratedValue
    private Long planPlaceId;

    private LocalDateTime planPlaceDate;

    private LocalDateTime planPlaceStayTime;

    private LocalDateTime planPlaceArriveTime;

//    @OneToMany
//    @JoinColumn(name = "recommend_place_id")
//    private List<RecommendPlace> recommendPlaceId = new ArrayList<>();

    @ManyToOne
    private RecommendPlace recommendPlace;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDayId;


}
