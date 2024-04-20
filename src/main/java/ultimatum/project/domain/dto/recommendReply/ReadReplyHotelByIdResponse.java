package ultimatum.project.domain.dto.recommendReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadReplyHotelByIdResponse {
    private Long recommendReplyStar;
    private List<String> recommendReplyTagValue;
    private Long recommendHotelId;
}
