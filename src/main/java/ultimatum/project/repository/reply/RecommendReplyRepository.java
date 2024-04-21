package ultimatum.project.repository.reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;

import java.util.List;

public interface RecommendReplyRepository extends JpaRepository<RecommendReply, Long> {

    // 특정 RecommendList~~~~ ID를 기반으로 모든 RecommendReply 객체들을 조회
    List<RecommendReply> findByRecommendFoodId_RecommendFoodId(Long recommendFoodId);
    List<RecommendReply> findByRecommendPlaceId_RecommendPlaceId(Long recommendPlaceId);
    List<RecommendReply> findByRecommendHotelId_RecommendHotelId(Long recommendHotelId);
    List<RecommendReply> findByRecommendEventId_RecommendEventId(Long recommendEventId);

    //태그 받기
    Page<RecommendReply> findByRecommendReplyTagValueContainingIgnoreCase(String recommendReplyTagValue, Pageable pageable);


}

