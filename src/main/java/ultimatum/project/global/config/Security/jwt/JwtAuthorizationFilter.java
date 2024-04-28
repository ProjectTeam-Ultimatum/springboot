package ultimatum.project.global.config.Security.jwt;

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
import ultimatum.project.global.config.Security.auth.PrincipalDetails;
import ultimatum.project.repository.member.MemberRepository;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final MemberRepository memberRepository;
	private final JwtProperties jwtProperties;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtProperties jwtProperties) {
		super(authenticationManager);
		this.memberRepository = memberRepository;
		this.jwtProperties = jwtProperties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		System.out.println("Header: " + header);
		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

		String email = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).build().verify(token)
				.getClaim("email").asString();

		System.out.println("email: " + email);

		if (email != null) {
			Member member = memberRepository.findByMemberEmail(email);

			if (member != null) {
				PrincipalDetails principalDetails = new PrincipalDetails(member);
				Authentication authentication =
						new UsernamePasswordAuthenticationToken(
								principalDetails,
								null,
								principalDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}
}
