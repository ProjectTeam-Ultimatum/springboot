package ultimatum.project.dto.reviewReplyDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReplyRequest {

    private String reviewReplyer;
    private String reviewReplyContent;
}