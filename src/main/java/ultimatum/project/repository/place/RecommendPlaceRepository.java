package ultimatum.project.repository.place;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.place.RecommendPlace;

public interface RecommendPlaceRepository extends JpaRepository<RecommendPlace, Long> {
}
