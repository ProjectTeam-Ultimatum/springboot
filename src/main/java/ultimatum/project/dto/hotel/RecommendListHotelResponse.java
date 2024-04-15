package ultimatum.project.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListHotelResponse {

    private String recommendHotelContentsId;
    private String recommendHotelTitle;
    private String recommendHotelIntroduction;
    private String recommendHotelAllTag;
    private String recommendHotelTag;
    private String recommendHotelCategory;
    private String recommendHotelAddress;
    private String recommendHotelRegion;
    private String recommendHotelLatitude;
    private String recommendHotelLongitude;
    private String recommendHotelPhoneNo;
    private String recommendHotelImgPath;

}
