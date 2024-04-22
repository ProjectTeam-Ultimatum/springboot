package ultimatum.project.domain.dto.logInDTO;

import lombok.Data;

@Data
public class MemberFindPasswordRequestDto {

    private String memberName;

    private String memberEmail;

    private String memberFindPasswordAnswer;

}
