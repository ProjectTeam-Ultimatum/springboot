package ultimatum.project.repository.food;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;

public interface RecommendListFoodRepository extends JpaRepository<RecommendListFood, Long> {

    Page<RecommendListFood> findByRecommendFoodTagContainingIgnoreCase(String tag, Pageable pageable);
    Page<RecommendListFood> findByRecommendFoodRegionContainingIgnoreCase(String region, Pageable pageable);
}




