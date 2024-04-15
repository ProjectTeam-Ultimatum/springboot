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
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long reviewImageId;

    private String imageName;

    private String imageUri;

    /**
     * fetchType.LAZY : 지연로딩
     *                  연관된 엔티티를 필요로 할때까지 로딩하지 않음
     *                  응답속도와 효율성을 높인다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

}