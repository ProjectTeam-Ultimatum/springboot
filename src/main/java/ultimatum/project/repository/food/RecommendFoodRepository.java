package ultimatum.project.repository.food;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendFood;

public interface RecommendFoodRepository extends JpaRepository<RecommendFood, Long> {
}
