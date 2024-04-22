package ultimatum.project.domain.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImageResponse {
    private Long reviewImageId;
    private String imageName;
    private String imageUri;
    private String imageUuid;

}
