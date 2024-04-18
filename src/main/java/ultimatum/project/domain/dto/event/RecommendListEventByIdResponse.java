package ultimatum.project.domain.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListEventByIdResponse {
    private Long recommendEventId;
    private String recommendEventTitle;
    private String recommendEventIntroduction;
    private String recommendEventAllTag;
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
