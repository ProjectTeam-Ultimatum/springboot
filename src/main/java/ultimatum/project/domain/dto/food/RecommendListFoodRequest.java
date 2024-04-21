package ultimatum.project.domain.dto.food;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.dto.image.RecommendImageFoodResponse;

import java.sql.Time;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListFoodRequest {

    private Long recommendFoodId;
    private String recommendFoodTitle;
    private String recommendFoodIntroduction;
    private String recommendFoodAllTag;
    private String recommendFoodTag;
    private String recommendFoodCategory;
    private String recommendFoodAddress;
    private String recommendFoodRegion;
    private Time recommendFoodOpentime;
    private Time recommendFoodClosetime;
    private String recommendFoodLatitude;
    private String recommendFoodLongitude;
    private String recommendFoodPhoneNo;
    private Long recommendFoodStar;
    private Long recommendFoodLike;
    private String recommendFoodImgPath;
}
