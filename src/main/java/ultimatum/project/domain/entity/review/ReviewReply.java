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
public class ReviewReply {

    @Id
    @GeneratedValue
    private Long reviewReplyId;

    private String reviewReplyer;

    private String reviewReplyContent;

}