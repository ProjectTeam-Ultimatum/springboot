package ultimatum.project.domain.dto.recommendReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.place.RecommendPlace;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadRecommendReplyByIdResponse {

    private Long recommendReplyId;
    private String recommendReply;
    private Long recommendReplyStar;
    private List<String> recommendReplyTagValue;
    private RecommendListPlace recommendPlaceId;
    private RecommendListFood recommendFoodId;
    private RecommendListHotel recommendHotelId;
    private RecommendListEvent recommendEventId;
}
