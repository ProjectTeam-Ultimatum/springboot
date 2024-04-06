package ultimatum.project.domain.entity.place;

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
public class RecommendPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_place_id")
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

    @OneToMany
    @JoinColumn(name = "place_base_tag_id")
    private List<PlaceTag> placeTags = new ArrayList<>();

}
