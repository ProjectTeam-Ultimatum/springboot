package ultimatum.project.domain.entity.recommendreply;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.place.RecommendPlace;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendReply {

    @Id
    @GeneratedValue
    private Long recommendReplyId;

    private String recommendReply;

    private Long recommendReplyStar;

    private String recommendReplyTagValue;

    @ManyToOne
    private RecommendPlace recommendPlaceId;

    @ManyToOne
    private RecommendFood recommendFoodId;

    @ManyToOne
    private RecommendHotel recommendHotelId;

}
