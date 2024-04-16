package ultimatum.project.domain.dto.logInDTO;

import lombok.Data;

@Data
public class MemberRequestDto {

    private String memberName;

    private String memberPassword;

    private String memberEmail;

    private Long memberAge;

    private String memberGender;

    private String memberAddress;

}
