package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;

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

    private LocalTime planEventStayTime;

    @ManyToOne
    @JoinColumn(name = "recommend_event_id")
    private RecommendListEvent recommendListEvents;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan planId;

}
