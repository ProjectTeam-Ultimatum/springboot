package ultimatum.project.dto.place;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.image.RecommendImage;
import ultimatum.project.dto.image.RecommendImageFoodResponse;
import ultimatum.project.dto.image.RecommendImagePlaceResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListPlaceResponse {

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
