package ultimatum.project.repository.place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;

public interface RecommendListPlaceRepository extends JpaRepository<RecommendListPlace, Long> {
    Page<RecommendListPlace> findByRecommendPlaceTagContainingIgnoreCase(String tag, Pageable pageable);
    Page<RecommendListPlace> findByRecommendPlaceRegionCase(String region, Pageable pageable);
}
