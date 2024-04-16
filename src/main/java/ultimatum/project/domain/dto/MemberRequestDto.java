package ultimatum.project.domain.dto;

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
