package ultimatum.project.domain.dto.recommendReply.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadReplyPlaceByIdResponse {
    private String recommendReply; //Security
    private Long recommendReplyStar;
    private Long recommendPlaceId;
}
