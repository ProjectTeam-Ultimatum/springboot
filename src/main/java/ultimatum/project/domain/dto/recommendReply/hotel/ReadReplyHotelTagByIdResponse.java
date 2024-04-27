package ultimatum.project.domain.dto.recommendReply.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadReplyHotelTagByIdResponse {
    private String recommendReply; //Security
    private List<String> recommendReplyTagValue;
    private Long recommendHotelId;
}
