package ultimatum.project.domain.entity.place;

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
public class RecommendPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendPlaceId;

    private String recommendPlaceTitle;

    private String recommendPlaceSubtitle;

    private String recommendPlaceAddress;

    private String recommendPlaceContent;

    private String recommendPlaceLatitude;

    private String recommendPlaceLongitude;

    private Long recommendPlaceLike;

    private Long recommendPlaceStar;

    private String recommendPlaceBudget;

    private String recommendPlaceCategory;

    @OneToMany(mappedBy = "recommendPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendImage> recommendImages = new ArrayList<>();

}
