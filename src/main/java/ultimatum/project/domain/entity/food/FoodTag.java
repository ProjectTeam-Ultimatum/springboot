package ultimatum.project.domain.entity.food;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_base_tag_id")
    private Long foodBaseTagId;

    private String foodBaseTagValue;
}
