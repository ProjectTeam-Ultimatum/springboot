package ultimatum.project.repository.hotel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;

public interface RecommendListHotelRepository extends JpaRepository<RecommendListHotel, Long> {

    Page<RecommendListHotel> findByRecommendHotelTagContainingIgnoreCase(String tag, Pageable pageable);
    Page<RecommendListHotel> findByRecommendHotelRegionContainingIgnoreCase(String region, Pageable pageable);
    RecommendListHotel findByRecommendHotelId(Long id); //ReplyService 숙박 평점 등록
    Page<RecommendListHotel> findByRecommendHotelTitleContainingIgnoreCase(String title, Pageable pageable);
}
