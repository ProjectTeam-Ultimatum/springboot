package ultimatum.project.domain.dto.recommendReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ultimatum.project.domain.entity.food.RecommendListFood;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReplyFoodResponse {

    private Long recommendReplyStar;
    private List<String> recommendReplyTagValue;
    private Long recommendFoodId;

}
