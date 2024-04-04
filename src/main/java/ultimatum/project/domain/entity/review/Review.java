package ultimatum.project.domain.entity.review;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue
    private Long reviewId;

    private String reviewTitle;

    private String reviewSubtitle;

    private String reviewContent;

    private Integer reviewLike;

    private String reviewLocation;

    @ManyToOne
    @JoinColumn(name = "review_image_id")
    private ReviewImage reviewImageid;

    @ManyToOne
    @JoinColumn(name = "review_reply_id")
    private ReviewReply reviewReplyId;


}