package ultimatum.project.dto.reviewReplyDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadReplyResponse {

    private Long reviewReplyId;

    private String reviewReplyer;

    private String reviewReplyContent;

    private LocalDateTime reg_date;

    private LocalDateTime mod_date;

}
