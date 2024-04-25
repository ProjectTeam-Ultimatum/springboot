package ultimatum.project.global.config.Security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ultimatum.project.domain.entity.member.Member;

import java.util.Date;

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


	public String generateToken(Member member) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JwtProperties.EXPIRATION_TIME);

		return JWT.create()
				.withSubject(Long.toString(member.getMemberId())) // 회원 ID를 subject로 사용
				.withClaim("name", member.getMemberName()) // 사용자 이름을 claim으로 추가
				.withClaim("email", member.getMemberEmail()) // 사용자 이메일을 claim으로 추가
				.withIssuedAt(now)
				.withExpiresAt(expiryDate)
				.sign(Algorithm.HMAC512(getSecret()));
	}
}
