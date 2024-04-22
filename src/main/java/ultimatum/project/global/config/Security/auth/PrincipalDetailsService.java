package ultimatum.project.global.config.Security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
		Member member = memberRepository.findByMemberEmail(memberName);

		return new PrincipalDetails(member);
	}


}
