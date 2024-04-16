package ultimatum.project.domain.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.review.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImageRequest {

    private String imageName;
    private String imageUri;
    private Review review;
}
