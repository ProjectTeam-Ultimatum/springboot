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
public class ReadReplyFoodByIdResponse {
    private String recommendReply; //Security
    private Long recommendReplyStar;
    private Long recommendFoodId;
}
