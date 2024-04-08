package ultimatum.project.dto.food;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendListDTO {
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
}
