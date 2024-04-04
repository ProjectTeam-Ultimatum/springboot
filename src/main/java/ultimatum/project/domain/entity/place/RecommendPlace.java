package ultimatum.project.domain.entity.place;

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
public class RecommendPlace {

    @Id
    @GeneratedValue
    private Long recommendPlaceId;

    private String recommendPlaceTitle;

    private String recommendPlaceSubtitle;

    private String recommendPlaceAddress;

    private String recommendPlaceContent;

    private Long recommendPlaceLatitude;

    private Long recommendPlaceLongitude;

    private Long recommendPlaceLike;

    private Long recommendPlaceStar;

    private Long recommendPlaceBudget;

//    @OneToMany
//    private Long placeBaseTagId;


}
