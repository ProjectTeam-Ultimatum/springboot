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

    /**
     * 왜 @OneToMany 를 써서 양방향으로 사용했는가?
     *      1. 연관된 데이터 접근의 편의성을 위해(하나의 엔티티가 여러관련 엔티티의 컬렉션을 직접 가질 때 유용
     *      2. 컬렉션에 대한 작업 : 컬렉션을 다룰때 엔티티간의 관계를 관리하고, 콜렉션에 속한 엔티티를 추가하거나 제거하는 작업을
     *      JPA가 자동으로 처리할수 있게 해준다.
     *      *** So, 리뷰를 생성하거나 수정하거나 삭제하거나 할때, 이미지에 대한 처리도 함께 해야하기 때문에 양방향을 써야한다. **
     */
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewReply> reviewReplies = new ArrayList<>();

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