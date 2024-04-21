package ultimatum.project.repository.food;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendListFood;

public interface RecommendListFoodRepository extends JpaRepository<RecommendListFood, Long> {
}
