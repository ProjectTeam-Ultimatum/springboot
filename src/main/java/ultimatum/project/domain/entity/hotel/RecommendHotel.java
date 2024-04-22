package ultimatum.project.domain.entity.hotel;

import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.image.RecommendImage;

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
    @GeneratedValue
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

    private String recommendHotelCategory;

    @OneToMany(mappedBy = "recommendHotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendImage> recommendImages = new ArrayList<>();

}