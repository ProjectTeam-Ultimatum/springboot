package ultimatum.project.dto.reviewReplyDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.review.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReplyRequest {

    private String reviewReplyer;
    private String reviewReplyContent;
}
