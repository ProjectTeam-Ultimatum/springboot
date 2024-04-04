package ultimatum.project.domain.entity.food;

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
public class FoodTag {

    @Id
    @GeneratedValue
    @Column(name = "food_base_tag_id")
    private Long foodBaseTagId;

    private String foodBaseTagValue;
}
