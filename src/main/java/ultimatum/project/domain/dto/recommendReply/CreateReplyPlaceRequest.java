package ultimatum.project.domain.dto.recommendReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReplyPlaceRequest {

    private Long recommendReplyStar;
    private List<String> recommendReplyTagValue;
    private Long recommendPlaceId;
}
