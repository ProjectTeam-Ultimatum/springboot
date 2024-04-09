package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.hotel.RecommendHotel;

public interface RecommendListHotelRepository extends JpaRepository<RecommendHotel, Long> {
}
