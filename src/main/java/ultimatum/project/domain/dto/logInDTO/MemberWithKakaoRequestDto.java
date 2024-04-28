package ultimatum.project.domain.dto.logInDTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MemberWithKakaoRequestDto {



    private Long memberAge;

    private String memberGender;

    private String memberAddress;

    private String memberFindPasswordAnswer;

    // 프로필 사진
    private List<MultipartFile> files;

}
