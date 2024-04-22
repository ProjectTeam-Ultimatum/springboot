package ultimatum.project.domain.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListPlaceResponse {

    private Long recommendPlaceId;
    private String recommendPlaceTitle;
    private String recommendPlaceIntroduction;
    private String recommendPlaceAllTag;
    private String recommendPlaceTag;
    private String recommendPlaceCategory;
    private String recommendPlaceAddress;
    private String recommendPlaceRegion;
    private String recommendPlaceLatitude;
    private String recommendPlaceLongitude;
    private String recommendPlacePhoneNo;
    private String recommendPlaceImgPath;

}
