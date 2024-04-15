package ultimatum.project.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewResponse {

    private Long reviewId;
    private String reviewTitle;
    private String reviewSubtitle;
    private String reviewContent;
    private String reviewLocation;
    private List<ReviewImageResponse> reviewImages; //이미지 메타데이터 정보리스트
}
