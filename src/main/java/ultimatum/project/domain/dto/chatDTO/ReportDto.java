package ultimatum.project.domain.dto.chatDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Long ReportId;
    private Long reporterId;
    private String reportedUserId;
    private String reportedMessage;
    private String reportReason;
}
