package ultimatum.project.domain.entity.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String memberPassword;

    private String memberName;

    private String memberEmail;

    private Long memberAge;

    private String memberGender;

    private String memberAddress;

    private String memberStyle;

    private String memberRole;

    public List<String> getRoleList(){
        if(this.memberRole.length() > 0){
            return Arrays.asList(this.memberRole.split(","));
        }
        return new ArrayList<>();
    }

}