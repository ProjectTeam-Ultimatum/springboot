package ultimatum.project.domain.dto.logInDTO;

import lombok.Data;

@Data
public class MemberUpdateRequestDto {

    private Long memberAge;  // 변경할 회원 나이
    private String memberAddress;  // 변경할 회원 주소

}
