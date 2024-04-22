package ultimatum.project.domain.dto.recommendReply.food;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.food.RecommendListFood;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReplyFoodRequest {

    //private String recommendReply;
    private Long recommendReplyStar;
    private List<String> recommendReplyTagValue;
    private Long recommendFoodId;

}
