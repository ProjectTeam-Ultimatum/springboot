package ultimatum.project.service.review;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.entity.review.Review;
import ultimatum.project.domain.entity.review.ReviewImage;
import ultimatum.project.dto.reviewDTO.*;
import ultimatum.project.repository.ReviewImageRepository;
import ultimatum.project.repository.ReviewRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;


    // 파일 저장 경로 설정 (환경에 따라 변경 가능)
    private final Path fileStorageLocation = Paths.get("uploads")
            .toAbsolutePath().normalize();

    @Transactional
    public CreateReviewResponse createReview(CreateReviewRequest request, List<MultipartFile> files) {
        // Review 객체 생성 및 저장
        Review review = new Review();
        review.setReviewTitle(request.getReviewTitle());
        review.setReviewSubtitle(request.getReviewSubtitle());
        review.setReviewContent(request.getReviewContent());
        review.setReviewLocation(request.getReviewLocation());
        review = reviewRepository.save(review); // 저장하고 리턴받음으로써 ID를 획득


        //ReviewImage 객체 리스트를 생성하기 위한 준비
        List<ReviewImage> reviewImages = new ArrayList<>();
        // 이미지 파일 처리 로직
        for (MultipartFile file : files) {     // request.getReviewImages() 배열 또는 리스트를 가져옵니다.

            try {
                String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String fileExtension = StringUtils.getFilenameExtension(originalFileName);
                String fileUuid = UUID.randomUUID().toString();
                String savedFileName = fileUuid + (fileExtension != null ? "." + fileExtension : "");


                // 파일 저장 위치 검증 및 생성
                if (!Files.exists(fileStorageLocation)) {
                    Files.createDirectories(fileStorageLocation);
                }

                // 파일 저장 경로
                Path targetLocation = fileStorageLocation.resolve(savedFileName);

                log.info("target:" + targetLocation);
                // 파일 저장
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


                // ReviewImage 객체 생성 및 저장
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReview(review);
                reviewImage.setUuid(fileUuid);
                reviewImage.setReviewFileName(originalFileName);
                reviewImages.add(reviewImage);

            } catch (IOException e) {

                throw new RuntimeException("이미지 저장 실패" + file.getOriginalFilename(), e);
            }
        }

        review.setReviewImages(reviewImages);
        reviewRepository.save(review);

        Review finalReview = review;
        reviewImages.forEach(image -> {
            image.setReview(finalReview);
            reviewImageRepository.save(image);
        });

        // CreateReviewResponse 객체 생성 및 반환
        return new CreateReviewResponse(review.getReviewId(),
                review.getReviewTitle(),
                review.getReviewSubtitle(),
                review.getReviewContent(),
                review.getReviewLocation(),
                reviewImages.stream().map(image ->
                        new ReviewImageResponse(
                                image.getReviewImageId(), image.getReviewFileName(), image.getUuid()
                        )
                ).collect(Collectors.toList())
        );

    }


    public List<ReadReviewResponse> getAllReviews() {

        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(review -> {
            List<ReviewImageResponse> images = review.getReviewImages().stream()
                    .map(image -> new ReviewImageResponse(
                            image.getReviewImageId(),
                            image.getUuid(),
                            image.getReviewFileName()
                    )).limit(1)
                    .collect(Collectors.toList());

            return new ReadReviewResponse(
                    review.getReviewTitle(),
                    review.getReviewSubtitle(),
                    review.getReviewContent(),
                    review.getReviewLike(),
                    review.getReviewLocation(),
                    images,
                    review.getReg_date(),
                    review.getMod_date()
            );
        }).collect(Collectors.toList());
    }


    public ReadReviewResponse getReviewById(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 게시글 ID를 찾을 수 없습니다." + reviewId));

        List<ReviewImageResponse> images = review.getReviewImages().stream()
                .map(image -> new ReviewImageResponse(
                        image.getReviewImageId(),
                        image.getUuid(),
                        image.getReviewFileName()
                ))    //첫번째 이미지만 포함
                .collect(Collectors.toList());

        return new ReadReviewResponse(
                review.getReviewTitle(),
                review.getReviewSubtitle(),
                review.getReviewContent(),
                review.getReviewLike(),
                review.getReviewLocation(),
                images,
                review.getReg_date(),
                review.getMod_date()

        );

    }

    @Transactional
    public UpdateReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, List<MultipartFile> newImages) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을수없습니다." + reviewId));

        review.update(request.getReviewTitle(), request.getReviewSubtitle(), request.getReviewContent(), request.getReviewLocation());
        //기존이미지 삭제
        review.getReviewImages().clear();


        //새 이미지들 저장
        for(MultipartFile file : newImages){
            //이미지 파일 저장 로직 (파일을 /upload 디렉토리에 저장)
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileUuid = UUID.randomUUID().toString();
            Path targetLocation = fileStorageLocation.resolve(fileUuid + "_"+fileName);
            try {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e) {
                throw new RuntimeException("파일저장에 실패했습니다. : " + fileName, e);
            }
            ReviewImage newImage = new ReviewImage();
            newImage.setReview(review);
            newImage.setUuid(fileUuid);
            newImage.setReviewFileName(fileName);
            review.getReviewImages().add(newImage);
        }

        reviewRepository.save(review);

        List<ReviewImageResponse> imageResponses = review.getReviewImages().stream()
                .map(image -> new ReviewImageResponse(image.getReviewImageId(), image.getUuid(), image.getReviewFileName()))
                .collect(Collectors.toList());

        return new UpdateReviewResponse(
                review.getReviewId(),
                review.getReviewTitle(),
                review.getReviewSubtitle(),
                review.getReviewContent(),
                review.getReviewLocation(),
                imageResponses
        );
    }



}