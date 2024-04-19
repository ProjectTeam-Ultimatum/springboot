package ultimatum.project.domain.dto.logInDTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MemberRequestDto {

    private String memberName;

    private String memberPassword;

    private String memberEmail;

    private Long memberAge;

    private String memberGender;

    private String memberAddress;

    // 프로필 사진
    private List<MultipartFile> files;

}
