package ultimatum.project.domain.entity.Map;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapDTO {
    Long id;
    String title;
    String addressCopy;
    Integer grade;
    String review;
    Double lon;
    Double lat;
}
