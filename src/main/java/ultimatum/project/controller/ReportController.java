package ultimatum.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ultimatum.project.domain.dto.chatDTO.ReportDto;
import ultimatum.project.global.config.Security.auth.PrincipalDetails;
import ultimatum.project.service.chat.ReportService;
import ultimatum.project.service.member.MemberService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/report")
    public ResponseEntity<?> submitReport(@RequestBody ReportDto reportDto,
                                          Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Long reporterId = principalDetails.getUser().getMemberId();  // 인증된 사용자 ID를 가져옵니다.
            reportDto.setReporterId(reporterId);  // 신고자 ID를 DTO에 설정합니다.

            // 신고 데이터 유효성 검증
            if (reportDto.getReportedUserId() == null || reportDto.getReportReason().isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid report data");
            }

            // 신고 내용을 처리하는 서비스 메소드 호출
            reportService.saveReport(reportDto);

            return ResponseEntity.ok("Report submitted successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
