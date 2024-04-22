package ultimatum.project.domain.entity.plan;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.place.RecommendListPlace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDateTime planEventStayTime;

    @ManyToOne
    @JoinColumn(name = "recommend_event_id")
    private RecommendListEvent recommendListEvents;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan planId;

}
