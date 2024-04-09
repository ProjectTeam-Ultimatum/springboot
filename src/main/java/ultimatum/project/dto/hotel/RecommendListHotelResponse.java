package ultimatum.project.dto.hotel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.image.RecommendImage;
import ultimatum.project.dto.image.RecommendImageHotelResponse;
import ultimatum.project.dto.image.RecommendImagePlaceResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListHotelResponse {

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
