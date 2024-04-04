package ultimatum.project.domain.entity.review;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImage {

    @Id
    @GeneratedValue
    private Long reviewImageId;

    private String reviewImagePath;
}