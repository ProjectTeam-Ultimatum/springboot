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
public class ReadReplyHotelByIdResponse {
    private String recommendReply; //Security
    private Long recommendReplyStar;
    private Long recommendHotelId;
}
