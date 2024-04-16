package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.review.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

}