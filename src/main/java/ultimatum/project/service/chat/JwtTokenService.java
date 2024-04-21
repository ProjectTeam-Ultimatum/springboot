package ultimatum.project.service.chat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.global.config.Security.jwt.JwtProperties;

@Service
public class JwtTokenService {

    private final Algorithm algorithm = Algorithm.HMAC512(JwtProperties.SECRET);

    // 토큰에서 사용자 정보를 파싱하여 반환
    public Member parseToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return buildMemberFromJWT(jwt);
        } catch (JWTVerificationException exception) {
            // 유효하지 않은 토큰인 경우 처리
            System.err.println("Invalid token: " + exception.getMessage());
            return null;
        }
    }

    // 토큰의 유효성 검사 //
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // JWT 클레임에서 Member 객체 생성
    private Member buildMemberFromJWT(DecodedJWT jwt) {
        Member member = new Member();
        member.setMemberId(jwt.getClaim("id").asLong());
        member.setMemberName(jwt.getClaim("username").asString());
        member.setMemberEmail(jwt.getClaim("userid").asString());
        member.setMemberGender(jwt.getClaim("gender").asString());
        member.setMemberRole(jwt.getClaim("role").asString());  // 'role' 클레임에서 역할 정보 추출
        return member;
    }
}