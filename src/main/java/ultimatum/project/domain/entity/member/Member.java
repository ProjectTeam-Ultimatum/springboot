package ultimatum.project.domain.entity.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private String memberId;

    private String memberPassword;

    private String memberName;

    private Long memberAge;

    private String memberGender;

    private String memberAddress;

    private String memberStyle;

    private String memberRule;
}