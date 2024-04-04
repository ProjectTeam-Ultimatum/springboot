package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}