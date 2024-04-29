package ultimatum.project.domain.dto.recommendReply.food;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadReplyFoodTagByIdResponse {
    private String recommendReply; //Security
    private List<String> recommendReplyTagValue;
    private Long recommendFoodId;
}
