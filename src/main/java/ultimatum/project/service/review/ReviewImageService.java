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
import ultimatum.project.repository.image.ReviewImageRepository;
import ultimatum.project.repository.review.ReviewRepository;
import ultimatum.project.service.S3.S3Service;

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
    public void updateReviewImages(Long reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 이미지 삭제 로직
        if (request.getDeleteImageIds() != null && !request.getDeleteImageIds().isEmpty()) {
            deleteImages(request.getDeleteImageIds(), review);
        }

        // 새 이미지 추가 로직
        if (request.getReviewImages() != null && !request.getReviewImages().isEmpty()) {
            List<ReviewImage> addedImages = createReviewImages(request.getReviewImages(), review);
            review.getReviewImages().addAll(addedImages);
        }
    }

    public List<ReviewImage> createReviewImages(List<MultipartFile> files, Review review) {
        List<ReviewImage> reviewImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String result = s3Service.uploadFileToS3(file);
                String[] parts = result.split(",");
                String fileUri = parts[0];
                String uuid = parts[1];

                ReviewImage reviewImage = ReviewImage.builder()
                        .review(review)
                        .imageName(StringUtils.cleanPath(file.getOriginalFilename()))
                        .imageUri(fileUri)
                        .uuid(uuid)
                        .build();
                reviewImages.add(reviewImage);
                imageRepository.save(reviewImage);
                log.info("Uploaded and saved new image: {}", file.getOriginalFilename());
            } catch (IOException e) {
                log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }
        return reviewImages;
    }

    public void deleteImages(List<String> deleteImageIds, Review review) {
        for (String imageIdStr : deleteImageIds) {

            Long imageId = Long.parseLong(imageIdStr);
            ReviewImage image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
            review.getReviewImages().removeIf(img -> img.getReviewImageId().equals(imageId));
            imageRepository.delete(image);
            s3Service.deleteFileFromS3(image.getImageUri());
            log.info("Deleted image ID: {}", imageId);
        }
    }


}
