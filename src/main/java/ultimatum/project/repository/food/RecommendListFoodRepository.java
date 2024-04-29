package ultimatum.project.repository.food;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendListFood;

public interface RecommendListFoodRepository extends JpaRepository<RecommendListFood, Long> {

    Page<RecommendListFood> findByRecommendFoodTagContainingIgnoreCase(String tag, Pageable pageable);
    Page<RecommendListFood> findByRecommendFoodRegionContainingIgnoreCase(String region, Pageable pageable);
    RecommendListFood findByRecommendFoodId(Long id); //ReplyService 음심점 평점 등록
    Page<RecommendListFood> findByRecommendFoodTitleContainingIgnoreCase(String title, Pageable pageable);
}





