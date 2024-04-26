package ultimatum.project.repository.plan;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.plan.PlanDay;
import ultimatum.project.domain.entity.plan.PlanHotel;
import ultimatum.project.repository.PlanRepository;

import java.util.Optional;

public interface PlanHotelRepository extends JpaRepository<PlanHotel, Long> {

    boolean existsByPlanDayAndRecommendHotel_RecommendHotelId(PlanDay planDay, Long recommendHotelId);

    @Query("SELECT COUNT(pf) > 0 FROM PlanHotel pf WHERE pf.planDay.planDayId = :planId AND pf.recommendHotel.recommendHotelId = :hotelId")
    boolean existsByPlanDayAndRecommendHotelId(Long planId, Long hotelId);

    Optional<PlanHotel> findByPlanDay(PlanDay planDay);
}
