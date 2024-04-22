package ultimatum.project.domain.entity.Map;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapDTO {
    private Long id;
    private String title;
    private String addressCopy;
    private Integer grade;
    private String review;
    private Double lonCopy;
    private Double latCopy;
    private String image;
    private List<String> mapTag;
    private String date;
    private Integer budget;
    private String course;
    private String category;

}
