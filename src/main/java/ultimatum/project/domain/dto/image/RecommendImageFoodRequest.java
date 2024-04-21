package ultimatum.project.domain.dto.image;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.place.RecommendPlace;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendImageFoodRequest {

    private String recommendImageUrl;
    private RecommendFood recommendFoodId;
    private RecommendPlace recommendPlaceId;
    private RecommendHotel recommendHotelId;

}