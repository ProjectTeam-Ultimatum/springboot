package ultimatum.project.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.dto.image.RecommendImagePlaceResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendPlaceResponse {

    private Long recommendPlaceId;
    private String recommendPlaceTitle;
    private String recommendPlaceSubtitle;
    private String recommendPlaceAddress;
    private String recommendPlaceContent;
    private String recommendPlaceLatitude;
    private String recommendPlaceLongitude;
    private Long recommendPlaceLike;
    private Long recommendPlaceStar;
    private String recommendPlaceBudget;
    private String recommendPlaceCategory;
    private List<RecommendImagePlaceResponse> recommendImageUrl; //manytoone
}
