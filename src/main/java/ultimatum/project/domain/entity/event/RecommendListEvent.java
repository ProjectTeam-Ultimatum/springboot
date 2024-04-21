package ultimatum.project.domain.entity.event;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendListEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_event_id")
    private Long recommendEventId;
    private String recommendEventContentsId;
    private String recommendEventTitle;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendEventIntroduction;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendEventAllTag;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendEventTag;
    private String recommendEventCategory;
    private String recommendEventAddress;
    private String recommendEventRegion;
    private Time recommendEventOpentime;
    private Time recommendEventClosetime;
    private Long recommendEventStar;
    private Long recommendEventLike;
    private String recommendEventLatitude;
    private String recommendEventLongitude;
    private String recommendEventPhoneNo;
    private String recommendEventImgPath;
    private Long recommendEventBudget;
}
