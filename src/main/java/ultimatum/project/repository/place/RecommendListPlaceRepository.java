package ultimatum.project.repository.place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.place.RecommendListPlace;

public interface RecommendListPlaceRepository extends JpaRepository<RecommendListPlace, Long> {
    // 특정 태그를 포함하는 관광지 목록을 페이지 단위로 반환
    Page<RecommendListPlace> findByRecommendPlaceTagContainingIgnoreCase(String tag, Pageable pageable);
    // 특정 지역을 포함하는 관광지 목록을 페이지 단위로 반환
    Page<RecommendListPlace> findByRecommendPlaceRegionContainingIgnoreCase(String region, Pageable pageable);
    // 특정 제목를 포함하는 관광지 목록을 페이지 단위로 반환
    Page<RecommendListPlace> findByRecommendPlaceTitleContainingIgnoreCase(String title, Pageable pageable);
    // 특정 ID를 가진 관광지 정보를 반환
    RecommendListPlace findByRecommendPlaceId(Long id); //ReplyService 관광지 평점 등록
}
