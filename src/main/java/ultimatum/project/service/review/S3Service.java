package ultimatum.project.service.review;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.entity.review.ReviewImage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;


@Service
@RequiredArgsConstructor
@Log4j2
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String bucketName = "ultimatum0807";

    /**
     * 파일을 S3에 업로드하고 해당 파일의 URI를 반환합니다.
     *
     * @param file MultipartFile - 업로드할 파일
     * @return String - 업로드된 파일의 URI
     * @throws IOException 파일 입력 스트림을 얻는 데 실패한 경우 발생
     */
    public String uploadFileToS3(MultipartFile file) throws IOException {
        // 파일의 이름이 null 인 경우 예외 발생
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename(), "File name cannot be null"));

        // S3에 파일 업로드: 파일의 저장 위치와 파일 이름
        String s3Key = "uploads/" + originalFileName;

        // 업로드할 파일의 메타데이터를 설정하는데 사용 (파일의 크기 설정)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        // 파일 스트림과 메타데이터를 함께 S3에 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata));
        log.info("업데이트 이미지파일 Uploading file to S3: {}", file.getOriginalFilename());

        // 성공적인 업로드 후 파일에 접근할 수 있는 URI 생성
        return amazonS3.getUrl(bucketName, s3Key).toString();
    }

    /**
     * S3에서 지정된 파일 URI에 해당하는 파일을 삭제합니다.
     *
     * @param imageUri String - 삭제할 파일의 완전한 URI
     */
    public void deleteFileFromS3(String imageUri) {
        try {
            //URL 객체 생성
            URL url = new URL(imageUri);
            // URL의 경로 부분에서 첫 번째 문자(보통 슬래시 '/')를 제거하여 실제 S3의 키를 얻습니다.
            // S3의 키는 버킷 내에서 객체를 유일하게 식별하는 문자열
            String key = url.getPath().substring(1);
            // S3 키가 URL 인코딩되어 있을 수 있으므로, 정확한 키를 얻기 위해 UTF-8 인코딩을 사용하여 디코딩
            key = URLDecoder.decode(key, UTF_8);
            // S3에서 객체 삭제
            amazonS3.deleteObject(bucketName, key);
            log.info("File deleted from S3: {}", imageUri);

        } catch (AmazonServiceException e) {
            log.error("Error deleting object {} from S3 bucket {}: {}", imageUri, bucketName, e.getErrorMessage());
            // AmazonServiceException 처리
        } catch (SdkClientException e) {
            log.error("Error deleting object {} from S3 bucket {}: {}", imageUri, bucketName, e.getMessage());
            // SdkClientException 처리
        } catch (MalformedURLException e) {
            log.error("Invalid URL for object {}: {}", imageUri, e.getMessage());
            // MalformedURLException 처리
        }
    }

}

