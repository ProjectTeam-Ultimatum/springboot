package ultimatum.project.repository.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.plan.PlanHotel;

public interface PlanHotelRepository extends JpaRepository<PlanHotel, Long> {
}
