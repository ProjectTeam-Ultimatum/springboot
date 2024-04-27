package ultimatum.project.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.chat.ChatRoom;
import ultimatum.project.domain.entity.chat.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    int countByReportedUserId(String email);
}
