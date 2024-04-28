package ultimatum.project.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.member.MemberImage;
import ultimatum.project.repository.member.MemberImageRepository;
import ultimatum.project.repository.member.MemberRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberImageService {

    private final MemberS3Service memberS3Service;

    @Transactional
    public List<MemberImage> createMemberImages(List<MultipartFile> files, Member member) {
        List<MemberImage> memberImages = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String fileUri = memberS3Service.uploadFileToS3(file);  // S3 업로드 로직을 서비스로 이동

                // MemberImage 객체 생성 및 저장
                MemberImage memberImage = new MemberImage();
                memberImage.setMemberImageName(StringUtils.cleanPath(file.getOriginalFilename()));
                memberImage.setMemberImageUrl(fileUri); //s3 파일 uri 설정
                memberImage.setMember(member);
                memberImages.add(memberImage); // 올바른 리스트에 객체를 추가

            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패" + file.getOriginalFilename(), e);
            }
        }
        return memberImages;
    }
}
