package ultimatum.project.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ultimatum.project.domain.dto.chatDTO.ReportDto;
import ultimatum.project.domain.entity.chat.Report;
import ultimatum.project.repository.chat.ReportRepository;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    // 리포지토리, 기타 컴포넌트 주입 생략...

    public void saveReport(ReportDto reportDto) {
        // Report 엔티티 생성 및 저장 로직
        Report report = new Report();
        report.setReporterId(reportDto.getReporterId());
        report.setReportedUserId(reportDto.getReportedUserId());
        report.setReportedMessage(reportDto.getReportedMessage());
        report.setReportReason(reportDto.getReportReason());
        // report.setReportStatus(ReportStatus.PENDING); // 예시: 상태 필드 추가 가능

        // 데이터베이스에 저장
        reportRepository.save(report);

        // 추가 로직: 알림 서비스, 신고 통계 업데이트 등...
    }

    public int countUserReports(String email) {
        return reportRepository.countByReportedUserId(email);
    }

}