package ultimatum.project.domain.dto.logInDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberWithKakaoResponseDto {

    private Long memberId;

    private String memberName;

    private String memberEmail;

    private Long memberAge;

    private String memberGender;

    private String memberAddress;

    private String memberFindPasswordAnswer;

    private String memberStyle;

    private String memberRole;

    private String jwtToken;  // JWT 토큰을 저장하는 필드

    // 프로필 사진
    private List<MemberImageResponseDto> files;

    private boolean newMember;


}
