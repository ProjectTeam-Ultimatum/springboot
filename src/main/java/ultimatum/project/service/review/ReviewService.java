package ultimatum.project.service.review;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final AmazonS3 amazonS3;
    private final String bucketName = "ultimatum0807"; // S3 버킷 이름 설정





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

                //s3에 파일 업로드
                String s3Key = "uploads/"+ originalFileName;
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                amazonS3.putObject(bucketName, s3Key, file.getInputStream(), metadata);

                //s3 파일 uri 생성
                String fileUri = amazonS3.getUrl(bucketName, s3Key).toString();


                // ReviewImage 객체 생성 및 저장
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setImageName(originalFileName);
                reviewImage.setImageUri(fileUri); //s3 파일 uri 설정
                reviewImage.setReview(review);
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
                                image.getReviewImageId(), image.getImageName(),image.getImageUri()
                        )
                ).collect(Collectors.toList())
        );

    }


    public Page<ReadReviewResponse> getAllReviews(Pageable pageable) {

        //페이지네이션을 적용하여 Review 엔티티들을 조회
        Page<Review> reviewPage = reviewRepository.findAll(pageable);

        //Review 엔티티들을 ReadReviewResponse DTO로 변환
        return reviewPage.map(review -> {
            //이미지중 하나씩만가져오기
            ReviewImageResponse imageResponse = review.getReviewImages().stream()
                    .map(image -> new ReviewImageResponse(
                            image.getReviewImageId(), image.getImageName(),image.getImageUri()

                    ))
                    .findFirst()
                    .orElse(null);

            //단일 이미지를 리스트에 넣음
            List<ReviewImageResponse> imageResponses = new ArrayList<>();

            return new ReadReviewResponse(
                    review.getReviewTitle(),
                    review.getReviewSubtitle(),
                    review.getReviewContent(),
                    review.getReviewLike(),
                    review.getReviewLocation(),
                    imageResponses,
                    review.getReg_date(),
                    review.getMod_date()
            );
        });
    }


    public ReadReviewResponse getReviewById(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 게시글 ID를 찾을 수 없습니다." + reviewId));

        List<ReviewImageResponse> images = review.getReviewImages().stream()
                .map(image -> new ReviewImageResponse(
                        image.getReviewImageId(), image.getImageName(),image.getImageUri()

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

            try {

                // multipart file 객체의 파일의 원본이름을 정제하는 과정
                String updateFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                //s3에 파일 업로드
                String s3Key = "uploads/"+ updateFileName;
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                amazonS3.putObject(bucketName, s3Key, file.getInputStream(), metadata);

                //s3 파일 uri 생성
                String fileUri = amazonS3.getUrl(bucketName, s3Key).toString();

                ReviewImage newImage = new ReviewImage();
                newImage.setReview(review);
                newImage.setImageName(updateFileName);
                newImage.setImageUri(fileUri);
                review.getReviewImages().add(newImage);


            }catch (IOException e) {
                throw new RuntimeException("파일저장에 실패했습니다. : " + file.getOriginalFilename(), e);
            }

        }

        reviewRepository.save(review);

        List<ReviewImageResponse> imageResponses = review.getReviewImages().stream()
                .map(image -> new ReviewImageResponse(
                        image.getReviewImageId(), image.getImageName(),image.getImageUri()
                ))
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

    @Transactional
    public DeleteReviewResponse deleteReview(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 게시글 ID를 찾을 수 없습니다." + reviewId));

        reviewRepository.delete(review);
        return new DeleteReviewResponse(reviewId);


    }



}