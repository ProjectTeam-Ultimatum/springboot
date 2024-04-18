package ultimatum.project.domain.dto.logInDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberImageResponseDto {

    private Long reviewImageId;
    private String imageName;
    private String imageUri;

}
