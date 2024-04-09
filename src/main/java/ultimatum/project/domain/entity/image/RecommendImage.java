package ultimatum.project.domain.entity.image;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.place.RecommendPlace;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendImageId;

    private String recommendImageUrl;

    @ManyToOne
    @JoinColumn(name = "recommend_food_id")
    private RecommendFood recommendFood;

    @ManyToOne
    @JoinColumn(name = "recommend_place_id")
    private RecommendHotel recommendPlace;

    @ManyToOne
    @JoinColumn(name = "recommend_hotel_id")
    private RecommendPlace recommendHotel;


}
