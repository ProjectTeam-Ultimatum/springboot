package ultimatum.project.global.config.Security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ultimatum.project.domain.dto.logInDTO.LoginRequestDto;
import ultimatum.project.global.config.Security.auth.PrincipalDetails;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtProperties jwtProperties;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 진입");

		ObjectMapper om = new ObjectMapper();
		LoginRequestDto loginRequestDto = null;
		try {
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getMemberEmail(),
						loginRequestDto.getMemberPassword());

		Authentication authentication =
				authenticationManager.authenticate(authenticationToken);

		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("Authentication : " + principalDetails.getUser().getMemberEmail());
		return authentication;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

		String jwtToken = JWT.create()
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetails.getUser().getMemberId())
				.withClaim("username", principalDetails.getUser().getMemberName())
				.withClaim("userid", principalDetails.getUser().getMemberEmail())
				.withClaim("gender", principalDetails.getUser().getMemberGender())
				.sign(Algorithm.HMAC512(jwtProperties.getSecret()));

		response.addHeader(jwtProperties.HEADER_STRING, jwtProperties.TOKEN_PREFIX + jwtToken);
	}
}
