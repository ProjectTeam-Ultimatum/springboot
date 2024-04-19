package ultimatum.project.domain.entity.member;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ultimatum.project.domain.entity.review.ReviewImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    private String memberFindPasswordAnswer;

    private String memberStyle;

    private String memberRole;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberImage> memberImages = new ArrayList<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = Arrays.stream(this.memberRole.split(","))
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
        return authorities;
    }

    public List<String> getRoleList() {
        if (this.memberRole != null && !this.memberRole.isEmpty()) {
            return Arrays.asList(this.memberRole.split(","));
        }
        return Arrays.asList("ROLE_USER");
    }
}