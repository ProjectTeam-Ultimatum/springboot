package ultimatum.project.domain.entity.recommendreply;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendReplyId;

    private String recommendReply;

    private Long recommendReplyStar;

    private String recommendReplyTagValue;

    @ManyToOne
    @JoinColumn(name = "recommend_place_id")
    private RecommendPlace recommendPlaceId;

    @ManyToOne
    @JoinColumn(name = "recommend_food_id")
    private RecommendFood recommendFoodId;

    @ManyToOne
    @JoinColumn(name = "recommend_hotel_id")
    private RecommendHotel recommendHotelId;

}
