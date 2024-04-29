package ultimatum.project.domain.dto.logInDTO;

import lombok.Data;

@Data
public class KakaoUserInfoDTO1 {
    private Long id;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Data
    public static class Properties {
        private String nickname; // "properties" 객체 내의 "nickname" 필드
    }

    @Data
    public static class KakaoAccount {
        private String email; // "kakao_account" 객체 내의 "email" 필드
        // ... 기타 계정 정보 필드
    }

    // 편의 메소드로 name과 email을 제공
    public String getMemberName() {
        return properties != null ? properties.getNickname() : null;
    }

    public String getMemberEmail() {
        return kakao_account != null ? kakao_account.getEmail() : null;
    }

}
