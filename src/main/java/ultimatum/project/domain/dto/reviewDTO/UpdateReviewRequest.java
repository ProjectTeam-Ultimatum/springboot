package ultimatum.project.domain.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewRequest {

    private String reviewTitle;
    private String reviewSubtitle;
    private String reviewContent;
    private String reviewLocation;
    private List<MultipartFile> ReviewImages; //이미지추가, 삭제, 업데이트를 위한 정보
    private List<String> deleteImageIds;
}
