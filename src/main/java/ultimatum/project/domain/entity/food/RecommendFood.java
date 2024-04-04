package ultimatum.project.domain.entity.food;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendFood {

    @Id
    @GeneratedValue
    private Long recommendFoodId;

    private String recommendFoodTitle;

    private String recommendFoodSubtitle;

    private String recommendFoodAddress;

    private String recommendFoodContent;

    private LocalDateTime recommendFoodOpentime;

    private LocalDateTime recommendFoodClosetime;

    private Long recommendFoodStar;

    private Long recommendFoodLatitude;

    private Long recommendFoodLongitude;

    private Long recommendFoodBudget;

//    @OneToMany
//    private FoodTag foodBaseTagId; 이거 모르겠음

}
