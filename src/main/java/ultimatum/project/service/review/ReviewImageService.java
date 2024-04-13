package ultimatum.project.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewImage;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.ReviewImageRepository;
import ultimatum.project.repository.ReviewReplyRepository;
import ultimatum.project.repository.ReviewRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewImageService {
    private final ReviewImageRepository imageRepository;
    private final ReviewRepository reviewRepository;

    private final S3Service s3Service;


    @Transactional
    public void updateImages(Long reviewId, List<MultipartFile> newImages, List<String> deleteImages) throws IOException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        //삭제할 이미지 ID처리
        if (deleteImages != null) {
            for (String imageIdStr : deleteImages) {
                Long imageId = Long.parseLong(imageIdStr);
                //이미지 존재확인
                ReviewImage image = imageRepository.findById(imageId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
                log.info("삭제할 이미지 id : " + imageId);
                //S3 이미지 삭제 로직
                s3Service.deleteFileFromS3(image.getImageUri());

                //데이터베이스에서 이미지 정보 삭제
                imageRepository.deleteById(imageId);
                log.info("Image with id: {} deleted from database", imageId);
            }
        }
        // 새이미지 추가
        if (newImages != null && !newImages.isEmpty()) {
            newImages.forEach(newImage -> {
                try {
                    String imageUrl = s3Service.uploadFileToS3(newImage);
                    ReviewImage image = ReviewImage.builder()
                            .review(review)
                            .imageName(newImage.getOriginalFilename())
                            .imageUri(imageUrl)
                            .build();
                    imageRepository.save(image);
                    log.info("Uploaded and saved image: {}", newImage.getOriginalFilename());
                } catch (IOException e) {
                    log.error("Failed to upload image: {}", newImage.getOriginalFilename(), e);
                    // Optionally rethrow to manage transaction rollback
                    throw new RuntimeException("Failed to upload image: " + newImage.getOriginalFilename(), e);
                }
            });
        } else {
            log.warn("No images provided for upload for reviewId: {}", reviewId);
        }
    }

    @Transactional
    public void deleteImages(List<String> deleteImages) {

        //삭제할 이미지 ID처리
        if (deleteImages != null) {
            for (String imageIdStr : deleteImages) {
                Long imageId = Long.parseLong(imageIdStr);
                ReviewImage image = imageRepository.findById(imageId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
                log.info("삭제할 이미지 id : " + imageId);
                //이미지 삭제 로직
                s3Service.deleteFileFromS3(image.getImageUri());

                //데이터베이스에서 이미지 정보 삭제
                imageRepository.delete(image);
            }
        }
    }
}
