package ultimatum.project.domain.dto.logInDTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MemberUpdateRequestDto {

    private String memberName;
    private String memberGender;
    private Long memberAge;  // 변경할 회원 나이
    private String memberAddress;  // 변경할 회원 주소
    private String findByPasswordAnswer;
    private List<MultipartFile> files;
}
