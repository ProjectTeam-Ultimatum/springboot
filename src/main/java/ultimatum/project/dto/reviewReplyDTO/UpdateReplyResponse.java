package ultimatum.project.dto.reviewReplyDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReplyResponse {

    private Long reviewReplyId;
    private String reviewReplyer;
    private String reviewReplyContent;
}
