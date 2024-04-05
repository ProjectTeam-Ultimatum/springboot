package ultimatum.project.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    private String reviewTitle;
    private String reviewSubtitle;
    private String reviewContent;
    private String reviewLocation;
    private List<MultipartFile> reviewImages;
}
