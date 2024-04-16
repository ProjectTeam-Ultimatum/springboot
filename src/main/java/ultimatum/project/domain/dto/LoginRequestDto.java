package ultimatum.project.domain.dto;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String memberEmail;
    private String memberPassword;

}
