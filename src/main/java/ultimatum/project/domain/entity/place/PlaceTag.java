package ultimatum.project.domain.entity.place;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceTag {
    @Id
    @GeneratedValue
    @Column(name = "place_base_tag_id")
    private Long placeBaseTagId;

    private String placeBaseTagValue;
}
