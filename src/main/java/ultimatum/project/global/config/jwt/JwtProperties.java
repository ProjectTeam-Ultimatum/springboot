package ultimatum.project.global.config.jwt;

public interface JwtProperties {
	String SECRET = "loose";  // 서버만 알고 있는 개인키
	int EXPIRATION_TIME = 864000000; // 10일 (1/1000초 단위)
	String TOKEN_PREFIX = "Bearer ";
	// JWT 토큰에는 통상적으로 앞에 "bearer "를 붙여주게 된다.
	// JWT를 쓴다라는 의미론적 의미를 담기 때문에 개발자들끼리 쉽게 이해가 가능하며 해당 값으로 인해 중간 탈취가 일어날 경우 토큰 무효화가 가능하다.
	// 더불어 bearer를 사전에 검사함으로서 JWT 토큰인지 아님을 빠르게 식별할 수 있게된다.
	String HEADER_STRING = "Authorization";
}