package ultimatum.project.domain.dto.recommendReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendReplyRequest {

    private Long recommendReplyId;
    private Long recommendReplyStar;
    private List<String> recommendReplyTagValue;
    private RecommendListPlace recommendPlaceId;
    private RecommendListFood recommendFoodId; // 4 request, response
    private RecommendListHotel recommendHotelId;
    private RecommendListEvent recommendEventId;
}
