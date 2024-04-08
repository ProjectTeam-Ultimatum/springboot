package ultimatum.project.dto.food;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.place.RecommendPlace;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendImageDTO {

    private Long recommendImageId;
    private String recommendImageUrl;
    private RecommendFood recommendFoodId;
    private RecommendHotel recommendPlaceId;
    private RecommendPlace recommendHotelId;

}