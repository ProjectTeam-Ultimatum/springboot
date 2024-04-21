package ultimatum.project.domain.dto.recommendReply.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadReplyEventAllRequest {
    private String recommendReply; //Security
    private Long recommendReplyStar;
    private List<String> recommendReplyTagValue;
    private Long recommendEventId;
}
