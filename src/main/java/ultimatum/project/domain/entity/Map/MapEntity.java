package ultimatum.project.domain.entity.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String review;
    private String addressCopy;
    private Integer grade;
    private double lon;
    private double lat;

    public void update(Long id, String title, String addressCopy, Integer grade, String review) {
        this.id = id;
        this.title = title;
        this.addressCopy = addressCopy;
        this.grade = grade;
        this.review = review;
    }
}
