package ultimatum.project.domain.entity.food;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    private RecommendFood recommendFoodId;

    private String menuName;

}
