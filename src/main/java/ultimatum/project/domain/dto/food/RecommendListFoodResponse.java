package ultimatum.project.domain.dto.food;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListFoodResponse {

    private Long recommendFoodId;
    private String recommendFoodContentsId;
    private String recommendFoodTitle;
    private String recommendFoodIntroduction;
    private String recommendFoodAllTag;
    private String recommendFoodTag;
    private String recommendFoodCategory;
    private String recommendFoodAddress;
    private String recommendFoodRegion;
    private Time recommendFoodOpentime;
    private Time recommendFoodClosetime;
    private Long recommendFoodStar;
    private Long recommendFoodLike;
    private String recommendFoodLatitude;
    private String recommendFoodLongitude;
    private String recommendFoodPhoneNo;
    private String recommendFoodImgPath;
    private Long recommendFoodBudget;
}
