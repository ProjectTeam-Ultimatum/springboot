package ultimatum.project.repository.food;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendListFood;

public interface RecommendListFoodRepository extends JpaRepository<RecommendListFood, Long> {
    Page<RecommendListFood> findByRecommendFoodTitleContainingIgnoreCase(String title, Pageable pageable);
}
