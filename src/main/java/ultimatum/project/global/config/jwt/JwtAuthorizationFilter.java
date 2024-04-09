package ultimatum.project.global.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.global.config.auth.PrincipalDetails;
import ultimatum.project.repository.MemberRepository;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final MemberRepository memberRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
		super(authenticationManager);
		this.memberRepository = memberRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

		String memberName = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
				.getClaim("memberName").asString();

		if (memberName != null) {
			Member user = memberRepository.findByMemberName(memberName);

			if (user != null) {
				PrincipalDetails principalDetails = new PrincipalDetails(user);
				Authentication authentication =
						new UsernamePasswordAuthenticationToken(
								principalDetails,
								null, // 패스워드는 모르니까 null 처리, 인증 용도 x
								principalDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}

}
