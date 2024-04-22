package ultimatum.project.repository.place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.place.RecommendListPlace;

public interface RecommendListPlaceRepository extends JpaRepository<RecommendListPlace, Long> {
    Page<RecommendListPlace> findByRecommendPlaceTitleContainingIgnoreCase(String title, Pageable pageable);

}
