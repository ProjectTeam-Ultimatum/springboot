package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}