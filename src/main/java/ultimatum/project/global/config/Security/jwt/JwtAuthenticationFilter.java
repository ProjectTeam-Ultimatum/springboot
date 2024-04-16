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
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;

	// 인증 요청시에 실행되는 함수 => /login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		System.out.println("JwtAuthenticationFilter : 진입");
		
		// request에 있는 username과 password를 파싱해서 자바 Object로 받기
		// 요청된 request 값을 통해 loginRequestDto에 저장한다.
		ObjectMapper om = new ObjectMapper();
		LoginRequestDto loginRequestDto = null;
		try {
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 유저네임패스워드 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getMemberEmail(),
						loginRequestDto.getMemberPassword());


		//formLogin 방식에서는 설정에서 loginProcessingUrl("/login_proc")를 호출하면 자동으로 loadUserByUsername가 실행되면서 로그인이 되었습니다.
		//JWT 방식에선 loadUserByUsername를 수동으로 호출해야 하기 때문에 아래와 같이 authenticate 메소드를 실행합니다.
		//authenticate 메소드는 위의 그림에서 3번인 AuthenticationManager가 실행합니다.

		// authenticate() 함수가 호출 되면 Authentication Provider가
		// UserDetailsService의 loadUserByUsername를 호출합니다.
		Authentication authentication = 
				authenticationManager.authenticate(authenticationToken);

		// 그리고 PrincipalDetails를 리턴해주면 미리 만들었던 UsernamePasswordAuthenticationToken의 두번째 파라미터(Credential Password)와
		// PrincipalDetails를(DB에서 가져온 값)의 getPassword() 함수로 비교해서 동일하면 Authentication 객체를 만들어서 로그인이 되는 원리입니다.

		PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("Authentication : "+principalDetailis.getUser().getMemberEmail());
		return authentication;
	}

	// JWT Token 생성해서 response에 담아주기
	// Authentication 객체가 성공적으로 만들어 졌다면 아래의 Success 메소드가 수행되고
	// 응답 값에 JWT 토큰을 만들어주고 사용자에게 전달해줍니다.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
//				.withSubject(principalDetailis.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetailis.getUser().getMemberId())
				.withClaim("username", principalDetailis.getUser().getMemberName())
				.withClaim("userid", principalDetailis.getUser().getMemberEmail())
				.withClaim("gender", principalDetailis.getUser().getMemberGender())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}
	
}
