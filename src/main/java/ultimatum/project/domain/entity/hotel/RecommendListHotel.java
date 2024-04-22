package ultimatum.project.domain.entity.hotel;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendListHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendHotelId;
    private String recommendHotelContentsId;
    private String recommendHotelTitle;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendHotelIntroduction;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendHotelAllTag;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String recommendHotelTag;
    private String recommendHotelCategory;
    private String recommendHotelAddress;
    private String recommendHotelRegion;
    private Time recommendHotelOpentime;
    private Time recommendHotelClosetime;
    private Long recommendHotelStar;
    private Long recommendHotelLike;
    private String recommendHotelLatitude;
    private String recommendHotelLongitude;
    private String recommendHotelPhoneNo;
    private String recommendHotelImgPath;
    private String recommendHotelBudget;
}
