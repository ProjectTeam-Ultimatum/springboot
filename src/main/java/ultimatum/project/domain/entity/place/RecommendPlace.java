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
    @GeneratedValue
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
    @JoinColumn(name = "placeBaseTagId")
    private List<PlaceTag> placeBaseTagId = new ArrayList<>();

}
