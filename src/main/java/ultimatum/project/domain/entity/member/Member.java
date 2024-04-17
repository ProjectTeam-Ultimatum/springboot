package ultimatum.project.domain.entity.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
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

    private String memberStyle;

    private String memberRole;

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = Arrays.stream(this.memberRole.split(","))
//                .map(role -> new SimpleGrantedAuthority(role))
//                .collect(Collectors.toList());
//        return authorities;
//    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.memberRole == null) {
            return Collections.emptyList();  // 또는 기본 권한 설정
        }
        return Arrays.stream(this.memberRole.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public List<String> getRoleList() {
        if (this.memberRole != null && !this.memberRole.isEmpty()) {
            return Arrays.asList(this.memberRole.split(","));
        }
        return Arrays.asList("ROLE_USER");
    }
}