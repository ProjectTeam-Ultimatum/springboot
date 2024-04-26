package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;

import java.time.LocalTime;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planEventId;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    private PlanDay planDay;

    @ManyToOne
    @JoinColumn(name = "recommend_event_id")
    private RecommendListEvent recommendEvent;

    private LocalTime planEventStayTime;

    // 생성자
    public PlanEvent(Plan plan, PlanDay planDay, RecommendListEvent recommendEvent, LocalTime planEventStayTime) {
        this.plan = plan;
        this.planDay = planDay;
        this.recommendEvent = recommendEvent;
        this.planEventStayTime = planEventStayTime;
    }
}
