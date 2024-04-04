package ultimatum.project.domain.entity.hotel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelTag {
    @Id
    @GeneratedValue
    private Long hotelBaseTagId;

    private String hotelBaseTagValue;
}
