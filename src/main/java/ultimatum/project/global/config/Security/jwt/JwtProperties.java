package ultimatum.project.global.config.Security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {
	// 인스턴스 변수로 변경
	@Value("${jwt.secret}")
	private String secret;

	public static final int EXPIRATION_TIME = 864000000; // 10일 (1/1000초 단위)
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";

	// 시크릿 값을 반환하는 메서드 추가
	public String getSecret() {
		return secret;
	}
}
