package ultimatum.project.repository.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;

import java.util.List;

public interface RecommendReplyRepository extends JpaRepository<RecommendReply, Long> {

    // 특정 RecommendFoodId에 해당하는 모든 RecommendReply 객체들을 조회
//   List<RecommendReply> findByRecommendFoodId(Long recommendFoodId);

    // 특정 RecommendListFood의 ID를 기반으로 모든 RecommendReply 객체들을 조회
//    List<RecommendReply> findByRecommendFood_Id(Long recommendFoodId);

    // 특정 RecommendListFood의 ID를 기반으로 모든 RecommendReply 객체들을 조회
    List<RecommendReply> findByRecommendFoodId_RecommendFoodId(Long recommendFoodId);

}

