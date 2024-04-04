package ultimatum.project.domain.entity.hotel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendHotel {

    @Id
    @GeneratedValue

    private Long recommendHotelId;

    private String recommendHotelTitle;

    private String recommendHotelSubtitle;

    private String recommendHotelAddress;

    private String recommendHotelContent;

    private Long recommendHotellAtitude;

    private Long recommendHotelLongitude;

    private Long recommendHotelLike;

    private Long recommendHotelStar;

    private Long recommendHotelBudget;

//    @OneToMany
//    private Long hotelBaseTagId;

}
