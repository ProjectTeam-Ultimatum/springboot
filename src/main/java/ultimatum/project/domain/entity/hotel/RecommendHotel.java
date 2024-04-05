package ultimatum.project.domain.entity.hotel;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_hotel_id")
    private Long recommendHotelId;

    private String recommendHotelTitle;

    private String recommendHotelSubtitle;

    private String recommendHotelAddress;

    private String recommendHotelContent;

    private String recommendHotellAtitude;

    private String recommendHotelLongitude;

    private Long recommendHotelLike;

    private Long recommendHotelStar;

    private String recommendHotelBudget;

    @OneToMany
    @JoinColumn(name = "hotel_base_tag_id")
    private List<HotelTag> hotelTags = new ArrayList<>();

}
