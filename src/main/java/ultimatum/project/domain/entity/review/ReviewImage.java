package ultimatum.project.domain.entity.review;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewImageId;

    private String uuid;

    private String reviewFileName;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review reviewId;

}