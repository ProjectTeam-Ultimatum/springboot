package ultimatum.project.domain.dto.recommendReply.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReplyHotelRequest {

    private Long recommendReplyStar;
    private List<String> recommendReplyTagValue;
    private Long recommendHotelId;
}
