package ultimatum.project.domain.entity.food;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendListFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_food_id")
    private Long recommendFoodId;
    private String recommendFoodContentsId;
    private String recommendFoodTitle;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendFoodIntroduction;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendFoodAllTag;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendFoodTag;
    private String recommendFoodCategory;
    private String recommendFoodAddress;
    private String recommendFoodRegion;
    private Time recommendFoodOpentime;
    private Time recommendFoodClosetime;
    private Long recommendFoodStar; //평점
    private Long recommendFoodLike;
    private String recommendFoodLatitude;
    private String recommendFoodLongitude;
    private String recommendFoodPhoneNo;
    private String recommendFoodImgPath;
    private Long recommendFoodBudget;

}
