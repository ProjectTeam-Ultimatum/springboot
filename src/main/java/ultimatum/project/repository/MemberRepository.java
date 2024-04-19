package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberEmail(String memberEmail);

    Member findByMemberNameAndMemberEmail(String memberName, String memberEmail);
}