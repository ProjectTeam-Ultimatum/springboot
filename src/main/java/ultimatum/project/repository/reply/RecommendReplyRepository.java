package ultimatum.project.repository.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;

import java.util.List;

public interface RecommendReplyRepository extends JpaRepository<RecommendReply, Long> {

    // 특정 RecommendFoodId에 해당하는 모든 RecommendReply 객체들을 조회
    //List<RecommendReply> findByRecommendFoodId_Id(Long recommendFoodId);
}

