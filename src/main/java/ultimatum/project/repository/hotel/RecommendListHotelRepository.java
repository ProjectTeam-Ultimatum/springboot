package ultimatum.project.repository.hotel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;

public interface RecommendListHotelRepository extends JpaRepository<RecommendListHotel, Long> {
    Page<RecommendListHotel> findByRecommendHotelTitleContainingIgnoreCase(String title, Pageable pageable);
}
