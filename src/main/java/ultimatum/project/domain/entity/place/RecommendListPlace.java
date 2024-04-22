package ultimatum.project.domain.entity.place;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendListPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendPlaceId;
    private String recommendPlaceContentsId;
    private String recommendPlaceTitle;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendPlaceIntroduction;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendPlaceAllTag;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendPlaceTag;
    private String recommendPlaceCategory;
    private String recommendPlaceAddress;
    private String recommendPlaceRegion;
    private Time recommendPlaceOpentime;
    private Time recommendPlaceClosetime;
    private Long recommendPlaceStar;
    private Long recommendPlaceLike;
    private String recommendPlaceLatitude;
    private String recommendPlaceLongitude;
    private String recommendPlacePhoneNo;
    private String recommendPlaceImgPath;
    private String recommendPlaceBudget;
}
