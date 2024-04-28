package ultimatum.project.global.config.Security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.member.MemberRepository;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class Oauth2UserService extends DefaultOAuth2UserService {


    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);
        return processKakaoUser(oauthUser, userRequest);
    }

    private OAuth2User processKakaoUser(OAuth2User oauthUser, OAuth2UserRequest userRequest) {
        Map<String, Object> attributes = oauthUser.getAttributes();
        log.info("OAuth2 User Attributes from Kakao: {}", attributes);

        // 사용자 정보의 유효성 검증
        validateAttributes(attributes);

        // 필요한 추가적인 로직 수행, 예를 들어, 데이터베이스에 사용자 정보 저장
        // 예제로는 간단히 사용자의 이메일을 키로 사용하여 사용자를 식별
        String email = (String) attributes.get("email");
        if (email == null) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED_USERS);
        }


        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"  // Kakao의 주요 식별자로 이메일 사용
        );
    }

    private void validateAttributes(Map<String, Object> attribute){
        if(!attribute.containsKey("email")){
            throw new CustomException(ErrorCode.UNAUTHENTICATED_USERS);
        }
    }}
