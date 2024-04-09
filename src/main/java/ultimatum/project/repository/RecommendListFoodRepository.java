package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendFood;

public interface RecommendListFoodRepository extends JpaRepository<RecommendFood, Long> {
}
