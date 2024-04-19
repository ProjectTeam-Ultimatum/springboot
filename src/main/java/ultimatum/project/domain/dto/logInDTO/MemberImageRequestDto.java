package ultimatum.project.domain.dto.logInDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.review.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberImageRequestDto {

    private String imageName;
    private String imageUri;
    private Review review;

}
