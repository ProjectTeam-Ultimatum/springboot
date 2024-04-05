package ultimatum.project.domain.entity.review;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewReplyId;

    private String reviewReplyer;

    private String reviewReplyContent;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review reviewId;

}