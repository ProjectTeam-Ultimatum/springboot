package ultimatum.project.domain.entity.place;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_base_tag_id")
    private Long placeBaseTagId;

    private String placeBaseTagValue;
}
