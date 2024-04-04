package ultimatum.project.domain.entity.food;

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
public class FoodTag {

    @Id
    @GeneratedValue
    private Long foodBaseTagId;

    private String foodBaseTagValue;
}
