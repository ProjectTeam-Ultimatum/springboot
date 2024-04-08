package ultimatum.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.image.RecommendImage;

import java.util.List;

public interface RecommendImageRepository extends JpaRepository<RecommendImage, Long> {
    //List<RecommendImage> findByRecommendFoodId(Long recommendFoodId);
}
