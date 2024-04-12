package ultimatum.project.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteImageResponse {
    private List<Long> reviewImageId;
}
