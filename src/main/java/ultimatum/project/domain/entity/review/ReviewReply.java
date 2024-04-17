package ultimatum.project.domain.entity.review;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReply {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long reviewReplyId;

    private String reviewReplyer;

    private String reviewReplyContent;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @CreationTimestamp
    private LocalDateTime reg_date;

    @UpdateTimestamp
    private LocalDateTime mod_date;

    public void update(String reviewReplyContent){
        this.reviewReplyContent = reviewReplyContent;

    }


}