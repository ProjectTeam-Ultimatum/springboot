package ultimatum.project.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.hotel.RecommendHotel;

public interface RecommendHotelRepository extends JpaRepository<RecommendHotel, Long> {
}
