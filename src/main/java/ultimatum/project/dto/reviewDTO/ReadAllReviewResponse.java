package ultimatum.project.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.dto.reviewReplyDTO.ReadReplyResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadAllReviewResponse {

    private Long reviewId;

    private String reviewTitle;

    private String reviewSubtitle;

    private String reviewContent;

    private Long reviewLike;

    private String reviewLocation;

    private List<ReviewImageResponse> reviewImages;

    private long replyCount;

    private List<ReadReplyResponse> replies;

    private LocalDateTime reg_date;

    private LocalDateTime mod_date;



}
