package ultimatum.project.domain.entity.image;

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
public class RecommendImage {
    @Id
    @GeneratedValue
    private Long recommendImageId;

    private String recommendImagePath;

    private String filename;

    @ManyToOne
    private RecommendFood recommendFoodId;

    @ManyToOne
    private RecommendHotel recommendPlaceId;

    @ManyToOne
    private RecommendPlace recommendHotelId;

}
