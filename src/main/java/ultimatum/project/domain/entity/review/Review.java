package ultimatum.project.domain.entity.review;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue
    private Long reviewId;

    private String reviewTitle;

    private String reviewSubtitle;

    private String reviewContent;

    private Long reviewLike;

    private String reviewLocation;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @CreationTimestamp
    private Date reg_date;

    @UpdateTimestamp
    private Date mod_date;

    public void update(String reviewTitle, String reviewSubtitle,
                       String reviewContent, String reviewLocation){
        this.reviewTitle = reviewTitle;
        this.reviewSubtitle = reviewSubtitle;
        this.reviewContent = reviewContent;
        this.reviewLocation = reviewLocation;
    }


}