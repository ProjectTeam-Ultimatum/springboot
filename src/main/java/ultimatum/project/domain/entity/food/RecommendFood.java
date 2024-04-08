package ultimatum.project.domain.entity.food;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Time;
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
    @Column(name = "recommend_food_id")
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