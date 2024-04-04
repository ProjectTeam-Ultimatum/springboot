package ultimatum.project.domain.entity.food;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodMenu {

    @Id
    @GeneratedValue
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "recommend_food_id")
    private RecommendFood recommendFoodId;

    private String menuName;

}
