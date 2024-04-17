package ultimatum.project.domain.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewResponse {
    private Long reviewId;
    private String reviewTitle;
    private String reviewSubtitle;
    private String reviewContent;
    private Long reviewLike;
    private String reviewLocation;
    private List<ReviewImageResponse> reviewImages;
    private String memberAccount;
}
