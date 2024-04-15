package ultimatum.project.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListEventResponse {

    private String recommendEventContentsId;
    private String recommendEventTitle;
    private String recommendEventIntroduction;
    private String recommendEventAllTag;
    private String recommendEventTag;
    private String recommendEventCategory;
    private String recommendEventAddress;
    private String recommendEventRegion;
    private String recommendEventLatitude;
    private String recommendEventLongitude;
    private String recommendEventPhoneNo;
    private String recommendEventImgPath;
}
