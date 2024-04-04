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
public class MemberImage {

    @Id
    @GeneratedValue
    private Long memberImageId;

    private String memberImageUrl;

}
