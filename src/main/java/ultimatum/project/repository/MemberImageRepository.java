package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.member.MemberImage;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}
