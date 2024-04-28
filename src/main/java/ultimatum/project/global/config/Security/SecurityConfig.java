package ultimatum.project.global.config.Security;

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
import ultimatum.project.global.config.Security.jwt.JwtAuthenticationFilter;
import ultimatum.project.global.config.Security.jwt.JwtAuthorizationFilter;
import ultimatum.project.global.config.Security.jwt.JwtProperties;
import ultimatum.project.global.config.Security.oauth2.OAuthFailureHandler;
import ultimatum.project.global.config.Security.oauth2.OAuthSuccessHandler;
import ultimatum.project.global.config.Security.oauth2.Oauth2UserService;
import ultimatum.project.repository.member.MemberRepository;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


	private final AuthenticationConfiguration authenticationConfiguration;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OAuthFailureHandler oAuthFailureHandler;

	@Autowired
	private OAuthSuccessHandler oAuthSuccessHandler;

	@Autowired
	private CorsConfig corsConfig;

	@Bean(name = "passwordEncoder")
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = "authenticationManager")
	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtProperties jwtProperties,Oauth2UserService oauth2UserService) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.addFilter(corsConfig.corsFilter())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(form -> form.disable())
				.httpBasic(httpBasic -> httpBasic.disable())
				.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProperties))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository, jwtProperties))
//				.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo ->
//						//사용자 정보를 가져올때 사용할 서비스 설정
//						userInfo.userService(oauth2UserService))
//						.successHandler(oAuthSuccessHandler)
//						.failureHandler(oAuthFailureHandler))
				.authorizeRequests()
		 		.requestMatchers("/api/v1/user/info").access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
				.requestMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
				.anyRequest().permitAll()
				.and()
				.build();
	}

}