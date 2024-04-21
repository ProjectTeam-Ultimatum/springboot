package ultimatum.project.domain.dto.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.place.RecommendPlace;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendImageFoodResponse {

    private String recommendImageUrl;
    //private RecommendFood recommendFoodId; //테이블, 필드
}
