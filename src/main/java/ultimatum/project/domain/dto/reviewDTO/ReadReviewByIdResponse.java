package ultimatum.project.domain.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.dto.reviewReplyDTO.ReadReplyResponse;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadReviewByIdResponse {

    private Long reviewId;

    private String reviewTitle;

    private String reviewSubtitle;

    private String reviewContent;

    private Long reviewLike;

    private String reviewLocation;

    private List<ReviewImageResponse> reviewImages;

    private List<ReadReplyResponse> replies;

    private LocalDateTime reg_date;

    private LocalDateTime mod_date;

    private String author;
}
