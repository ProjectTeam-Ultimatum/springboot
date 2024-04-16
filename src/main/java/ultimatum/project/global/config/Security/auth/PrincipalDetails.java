/**
 * PrincipalDetails 클래스는 주로 데이터베이스에서 사용자 정보를 가져와서 Spring Security에 제공하는 역할을 수행합니다.
 * Member 객체와 PrincipalDetails 객체 간의 매핑을 통해 데이터베이스에 저장된 사용자 정보를
 * Spring Security가 이해할 수 있는 형태로 변환하여 제공합니다.
 */
package ultimatum.project.global.config.Security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ultimatum.project.domain.entity.member.Member;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails{

	private Member member;

    public PrincipalDetails(Member member){
        this.member = member;
    }

    public Member getUser() {
		return member;
	}

    @Override
    public String getPassword() {
        return member.getMemberPassword();
    }

    @Override
    public String getUsername() {
        return member.getMemberEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        member.getRoleList().forEach(r -> {
        	authorities.add(()->{ return r;});
        });
        return authorities;
    }
}
