package ultimatum.project.domain.entity.member;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberImage {

    @Id
    @GeneratedValue
    private Long memberImageId;

    private String memberImageUrl;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

}
