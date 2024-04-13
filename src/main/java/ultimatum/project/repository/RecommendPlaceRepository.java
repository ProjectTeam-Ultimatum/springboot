package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.place.RecommendPlace;

public interface RecommendPlaceRepository extends JpaRepository<RecommendPlace, Long> {
}
