package ultimatum.project.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListPlaceResponse {

    private String recommendPlaceTitle;
    private String recommendPlaceIntroduction;
    private String recommendPlaceAllTag;
    private String recommendPlaceTag;
    private String recommendPlaceCategory;
    private String recommendPlaceAddress;
    private String recommendPlaceRegion;
    private Time recommendPlaceOpentime;
    private Time recommendPlaceClosetime;
    private String recommendPlaceLatitude;
    private String recommendPlaceLongitude;
    private String recommendPlacePhoneNo;
    private String recommendPlaceImgPath;

}
