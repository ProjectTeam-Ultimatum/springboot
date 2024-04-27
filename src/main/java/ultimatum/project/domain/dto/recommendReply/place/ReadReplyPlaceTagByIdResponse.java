package ultimatum.project.domain.dto.recommendReply.place;

import lombok.*;

import java.util.List;
import java.util.StringJoiner;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadReplyPlaceTagByIdResponse {
    private String recommendReply; //Security
    private List<String> recommendReplyTagValue;
    private Long recommendPlaceId;
}
