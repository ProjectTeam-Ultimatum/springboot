package ultimatum.project.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.review.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImageRequest {

    private String uuid;
    private String filePath;
    private String reviewFileName;
    private Review review;
}
