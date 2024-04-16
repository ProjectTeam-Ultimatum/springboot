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

	@Bean(name = "passwordEncoder")
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = "authenticationManager")
	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http
				.csrf((csrfConfig) ->
						csrfConfig.disable()
				)
				.addFilter(corsConfig.corsFilter())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(form -> form.disable())
				.httpBasic(httpBasic -> httpBasic.disable())
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))
				.authorizeRequests()
				.requestMatchers("/api/v1/user/info")
				.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
				.requestMatchers("/api/v1/admin/**")
				.access("hasRole('ROLE_ADMIN')")
				.anyRequest().permitAll()
				.and().build();

	}
}