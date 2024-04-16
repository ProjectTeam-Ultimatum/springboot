package ultimatum.project.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewImage;
import ultimatum.project.domain.dto.reviewDTO.UpdateReviewRequest;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.ReviewImageRepository;
import ultimatum.project.repository.ReviewRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewImageService {
    private final ReviewImageRepository imageRepository;
    private final ReviewRepository reviewRepository;

    private final S3Service s3Service;


    @Transactional
    public List<ReviewImage> createReviewImages(List<MultipartFile> files, Review review) {
        List<ReviewImage> reviewImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileUri = s3Service.uploadFileToS3(file);  // S3 업로드 로직을 서비스로 이동

                // ReviewImage 객체 생성 및 저장
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setImageName(StringUtils.cleanPath(file.getOriginalFilename()));
                reviewImage.setImageUri(fileUri); //s3 파일 uri 설정
                reviewImage.setReview(review);
                reviewImages.add(reviewImage);

            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패" + file.getOriginalFilename(), e);
            }
        }
        return reviewImages;
    }


    @Transactional
    public void updateImages(Long reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));


        //삭제할 이미지 ID처리
        if (request.getDeleteImageIds() != null) {
            for (String imageIdStr : request.getDeleteImageIds()) {
                Long imageId = Long.parseLong(imageIdStr);
                //이미지 존재확인
                ReviewImage image = imageRepository.findById(imageId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
                log.info("삭제할 이미지 id : " + imageId);


                // Review 엔티티에서 이미지 정보 삭제
                review.getReviewImages().removeIf(img -> img.getReviewImageId().equals(imageId));

                // 데이터베이스에서 이미지 정보 삭제
                imageRepository.delete(image);

                //S3 이미지 삭제 로직
                s3Service.deleteFileFromS3(image.getImageUri());


                log.info("Image with id: {} deleted from database", imageId);
            }
        }
        // 새이미지 추가
        if (request.getReviewImages() != null && !request.getReviewImages().isEmpty()) {
            for (MultipartFile newImage : request.getReviewImages()) {
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
            }

        }

    }

}
