package ultimatum.project.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ultimatum.project.global.config.jwt.JwtAuthenticationFilter;
import ultimatum.project.global.config.jwt.JwtAuthorizationFilter;
import ultimatum.project.repository.MemberRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


	private final AuthenticationConfiguration authenticationConfiguration;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private CorsConfig corsConfig;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http
				/**
		 		* csrf 공격에 관한 방어 해제
				* REST API에서는 CSRF 방어가 필요가 없고 더불어 CSRF 토큰을 주고 받을 필요가 없기 때문에 CSRF 설정 해제
 		 		*/
				.csrf((csrfConfig) ->
						csrfConfig.disable()
				)
				/**
		 		* REST API에서는 여러 서버를 운영하는 환경이다보니 SOP 뿐만아니라 CORS도 허용을 해야
		 		* 여러 곳에서 접근이 가능하므로 CORS 허용
 		 		*/
				.addFilter(corsConfig.corsFilter())

				/**
		 		* 서버를 Stateless하게 유지합니다. 이걸 설정하면 Spring Security에서 세션을 만들지 않음
		 		* 만약에 켜둔다면 JWT Token으로 로그인하더라도 클라이언트에서 Token값을 서버에 전달하지 않더라도 세션 값으로 로그인 됨
		 		*/
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


				/**
				 *  formLogin을 끄면 초기 로그인 화면이 사라진다
				 * 	formLogin은 세션 로그인 방식에서 로그인을 자동처리 해준다는 장점이 존재했는데,
				 * 	JWT에서는 로그인 방식 내에 JWT 토큰을 생성하는 로직이 필요하기 때문에
				 * 	로그인 과정을 수동으로 클래스를 만들어줘야 하기 때문에 formLogin 기능을 제외
				 */
				.formLogin(form -> form.disable())

				/**
				 * 쿠키와 세션을 이용한 방식이 아니라 request header에 id와 password값을 직접 날리는 방식이라 보안에 굉장히 취약하다
				 * REST API에서는 오로지 토큰 방식을 이용하기 때문에 보안에 취약한 httpBasic 방식은 해제한다고 보면 된다
				 */
				.httpBasic(httpBasic -> httpBasic.disable())

				/**
				 * 로그인 요청이 들어오면
				 * UsernamePasswordAuthenticationFilter를 상속한 JwtAuthenticationFilter 클래스가 실행
				 * 안에서 JWT 토큰을 만들고 로그인 과정을 거친다.
				 * JWT에서는 로그인 흐름에 JWT 토큰을 발급하는 것을 추가해야 되기 때문에
				 * forLogin 자동 처리 방식이 아닌 클래스를 수동으로 개발하는 과정이 필요
				 */
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))

				/**
				 * AuthorizationFilter는 로그인이 아닌 사용자가 페이지 이동 시 인가 처리를 받기 위한 필터
				 * JWT에서는 클라이언트에서 API를 요청할 때마다 JWT 토큰 값을 매번 확인해야하므로
				 * BasicAuthenticationFilter를 구현해서 매 페이지 요청마다 아래처럼 토큰을 확인해야 함
				 */
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))

				/**
				 * URL에 따른 페이지 권한을 부여하기 위해 시작하는 메서드
				 */
				.authorizeRequests()

				/**
				 * 특정 URL에 관한 페이지 권한 부여를 위한 메서드
				 */
				.requestMatchers("/api/v1/user/info")

				/**
				 * 위의 경로에 접근이 가능한 Role 선언
				 */
				.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")

				/**
				 * 특정 URL에 관한 페이지 권한 부여를 위한 메서드
				 */
				.requestMatchers("/api/v1/admin/**")

				/**
				 * 위의 경로에 접근이 가능한 Rol 선언
				 */
				.access("hasRole('ROLE_ADMIN')")

				/**
				 * 특정 URL 이외에는 모든 페이지 접근 허가
				 */
				.anyRequest().permitAll()
				.and().build();

	}
}