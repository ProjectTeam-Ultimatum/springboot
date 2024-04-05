package ultimatum.project.domain.entity.hotel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_base_tag_id")
    private Long hotelBaseTagId;

    private String hotelBaseTagValue;
}
