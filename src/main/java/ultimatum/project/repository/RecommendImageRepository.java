package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.image.RecommendImage;
import ultimatum.project.domain.entity.place.RecommendPlace;

import java.util.List;

public interface RecommendImageRepository extends JpaRepository<RecommendImage, Long> {
    //RecommendImage findByFoodId(Long recommendFoodId);
    //List<RecommendImage> findByRecommendFoodId(Long recommendFoodId);

    //List<RecommendImage> findRecommendImageByRecommendFoodId(Long foodId);
    List<RecommendImage> findByRecommendFood(RecommendFood recommendFood);
    List<RecommendImage> findByRecommendHotel(RecommendHotel recommendHotel);
    List<RecommendImage> findByRecommendPlace(RecommendPlace recommendPlace);
}
