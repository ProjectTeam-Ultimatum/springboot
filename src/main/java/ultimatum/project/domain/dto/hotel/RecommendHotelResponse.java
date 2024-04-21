package ultimatum.project.domain.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.dto.image.RecommendImageHotelResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendHotelResponse {

    private Long recommendHotelId;
    private String recommendHotelTitle;
    private String recommendHotelSubtitle;
    private String recommendHotelAddress;
    private String recommendHotelContent;
    private String recommendHotellAtitude;
    private String recommendHotelLongitude;
    private Long recommendHotelLike;
    private Long recommendHotelStar;
    private String recommendHotelBudget;
    private String recommendHotelCategory;
    private List<RecommendImageHotelResponse> recommendImageUrl; //manytoone

}
