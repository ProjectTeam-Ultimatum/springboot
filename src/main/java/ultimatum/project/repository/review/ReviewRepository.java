package ultimatum.project.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ultimatum.project.domain.entity.review.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByReviewLocation (String reviewLocation, Pageable pageable);
    Page<Review> findByReviewTitleContainingIgnoreCaseOrReviewSubtitleContainingIgnoreCaseOrReviewContentContainingIgnoreCaseOrReviewLocationContainingIgnoreCase(String title, String subtitle, String content, String location,Pageable pageable);

}