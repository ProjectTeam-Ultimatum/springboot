package ultimatum.project.domain.entity.food;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String recommendFoodLatitude;

    private String recommendFoodLongitude;

    private String recommendFoodBudget;

    @OneToMany
    @JoinColumn(name = "food_base_tag_id")
    private List<FoodTag> foodBaseTagId = new ArrayList<>();

}
