package ultimatum.project.domain.dto.logInDTO;

import lombok.Data;

import java.util.List;

@Data
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

    // 프로필 사진
    private List<MemberImageResponseDto> files;

    private boolean newMember;
}
