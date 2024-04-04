package ultimatum.project.domain.entity.place;

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
    private Long placeBaseTagId;

    private String placeBaseTagValue;
}
