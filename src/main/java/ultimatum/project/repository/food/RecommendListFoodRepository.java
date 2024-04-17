package ultimatum.project.repository.food;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;

public interface RecommendListFoodRepository extends JpaRepository<RecommendListFood, Long> {

    //ContainingIgnoreCase
    //데이터베이스 쿼리에서 대소문자를 구분하지 않고 특정 필드에 특정 문자열이 포함되어 있는지 검사
    Page<RecommendListFood> findByRecommendFoodTagContainingIgnoreCase(String tag, Pageable pageable);
    Page<RecommendListFood> findByRecommendFoodRegionContainingIgnoreCase(String region, Pageable pageable);
}
