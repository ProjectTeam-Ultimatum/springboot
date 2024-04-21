package ultimatum.project.domain.dto.food;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
//@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendFoodResponse {
    private Long recommendFoodId;
    private String recommendFoodTitle;
    private String recommendFoodSubtitle;
    private String recommendFoodAddress;
    private String recommendFoodContent;
    private Time recommendFoodOpentime;
    private Time recommendFoodClosetime;
    private Long recommendFoodStar;
    private String recommendFoodLatitude;
    private String recommendFoodLongitude;
    private String recommendFoodBudget;
    private String recommendFoodCategory;
    //private List<RecommendImageFoodResponse> recommendImageResponses;
    private List<RecommendImageFoodResponse> recommendImageUrl; //manytoone
}
