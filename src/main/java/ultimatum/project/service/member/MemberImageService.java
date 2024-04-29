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
    private final MemberImageRepository imageRepository;

    @Transactional
    public List<MemberImage> createMemberImages(List<MultipartFile> files, Member member) {
        List<MemberImage> memberImages = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // 고유한 파일명 생성
                String uniqueFileName = generateUniqueFileName(member, file);

                // S3에 고유한 파일명으로 파일 업로드
                String fileUri = memberS3Service.uploadFileToS3(uniqueFileName, file);

                // MemberImage 객체 생성 및 저장
                MemberImage memberImage = new MemberImage();
                memberImage.setMemberImageName(uniqueFileName); // 고유한 파일명 설정
                memberImage.setMemberImageUrl(fileUri);
                memberImage.setMember(member);
                memberImages.add(memberImage); // 리스트에 객체 추가

            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패" + file.getOriginalFilename(), e);
            }
        }
        return memberImages;
    }


    @Transactional
    public MemberImage updateMemberImage(Member member, MultipartFile file) {
        try {
            // 기존 이미지가 있는지 확인하고 S3에서 삭제
            MemberImage existingImage = member.getMemberImages().stream().findFirst().orElse(null);
            if (existingImage != null) {
                memberS3Service.deleteFileFromS3(existingImage.getMemberImageUrl());
                imageRepository.delete(existingImage);
            }

            // 새 이미지를 S3에 업로드하고 데이터베이스에 저장
            String uniqueFileName = generateUniqueFileName(member, file); // 고유한 파일명 생성
            String fileUri = memberS3Service.uploadFileToS3(uniqueFileName, file); // S3에 업로드

            MemberImage newImage = new MemberImage();
            newImage.setMemberImageName(StringUtils.cleanPath(file.getOriginalFilename()));
            newImage.setMemberImageUrl(fileUri);
            newImage.setMember(member);

            return imageRepository.save(newImage);

        } catch (IOException e) {
            log.error("이미지 업데이트 실패: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("이미지 업데이트 실패: " + file.getOriginalFilename(), e);
        }
    }

    // 회원 정보와 파일을 이용하여 고유한 파일명 생성
    private String generateUniqueFileName(Member member, MultipartFile file) {
        // 회원 ID와 현재 타임스탬프를 이용하여 파일명 생성
        String timestamp = String.valueOf(System.currentTimeMillis());
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = member.getMemberId() + "_" + timestamp + "_" + originalFilename;
        return uniqueFileName;
    }

}
