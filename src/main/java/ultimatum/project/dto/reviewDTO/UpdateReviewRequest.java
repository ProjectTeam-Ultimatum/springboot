package ultimatum.project.dto.reviewDTO;

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
    private List<ImageInfo> Images; //이미지추가, 삭제, 업데이트를 위한 정보

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageInfo {
        private Long id; //이미지 ID, 새 이미지는 Null 또는 특정값 (-1등)
        private String action; // add, delete, update
        private String fileName; // 파일이름
    }

}
