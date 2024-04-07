package ultimatum.project.domain.entity.review;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ultimatum.project.dto.reviewDTO.ReviewImageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String reviewTitle;

    private String reviewSubtitle;

    private String reviewContent;

    private Long reviewLike;

    private String reviewLocation;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime reg_date;

    @UpdateTimestamp
    private LocalDateTime mod_date;

    @PrePersist
    public void prePersist() {
        // 엔티티가 처음 저장될 때 reviewLike를 0으로 설정
        if (reviewLike == null) {
            reviewLike = 0L;
        }
    }

    public void update(String reviewTitle, String reviewSubtitle,
                       String reviewContent, String reviewLocation){
        this.reviewTitle = reviewTitle;
        this.reviewSubtitle = reviewSubtitle;
        this.reviewContent = reviewContent;
        this.reviewLocation = reviewLocation;

    }


}