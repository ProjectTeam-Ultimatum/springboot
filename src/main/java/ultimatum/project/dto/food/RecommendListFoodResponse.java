package ultimatum.project.dto.food;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendListFoodResponse {

    private String recommendFoodContentsId;
    private String recommendFoodTitle;
    private String recommendFoodIntroduction;
    private String recommendFoodAllTag;
    private String recommendFoodTag;
    private String recommendFoodCategory;
    private String recommendFoodAddress;
    private String recommendFoodRegion;
    private String recommendFoodLatitude;
    private String recommendFoodLongitude;
    private String recommendFoodPhoneNo;
    private String recommendFoodImgPath;
}
