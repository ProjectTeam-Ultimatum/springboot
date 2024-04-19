package ultimatum.project.service.recommned;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.food.RecommendListFoodByIdResponse;
import ultimatum.project.domain.dto.place.RecommendListPlaceByIdResponse;
import ultimatum.project.domain.dto.recommendReply.*;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendFoodRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.hotel.RecommendListHotelRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.repository.reply.RecommendReplyRepository;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Service
@RequiredArgsConstructor //공통
@Transactional
public class RecommendReplyService {

    private RecommendReplyRepository recommendReplyRepository;
    private RecommendListFoodRepository recommendListFoodRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void RecommendReplyService(RecommendReplyRepository recommendReplyRepository,
                                      RecommendListFoodRepository recommendListFoodRepository,
                                      ModelMapper modelMapper) {
        this.recommendReplyRepository = recommendReplyRepository;
        this.recommendListFoodRepository = recommendListFoodRepository;
        this.modelMapper = modelMapper;
    }

    //음식 평점 저장 - 최종
    public CreateReplyFoodResponse createFoodReply(CreateReplyFoodRequest request) {
        // 요청에서 제공된 음식 ID를 기반으로 기존의 추천 후기를 가져옵니다.
        RecommendReply recommendReply = recommendReplyRepository.findById(request.getRecommendFoodId())
                .orElseThrow(() -> new IllegalArgumentException("음식점 평점 정보 없음"));

        // 요청에서 제공된 음식 ID를 기반으로 RecommendListFood를 가져옵니다.
        RecommendListFood recommendFood = recommendListFoodRepository.findByRecommendFoodId(request.getRecommendFoodId());

        // 요청 데이터를 기존의 RecommendReply 엔티티로 매핑합니다.
        recommendReply.setRecommendReplyStar(request.getRecommendReplyStar());
        recommendReply.setRecommendReplyTagValue(request.getRecommendReplyTagValue());
        recommendReply.setRecommendFoodId(recommendFood); // RecommendListFood 엔티티 설정

        // 수정된 엔티티를 저장합니다.
        recommendReply = recommendReplyRepository.save(recommendReply);

        // 응답 객체를 수동으로 생성합니다.
        return new CreateReplyFoodResponse(
                recommendReply.getRecommendReplyStar(),
                recommendReply.getRecommendReplyTagValue() != null
                        ? recommendReply.getRecommendReplyTagValue() // JSON을 List<String>로 변환
                        : List.of(),
                recommendReply.getRecommendFoodId() != null
                        ? recommendReply.getRecommendFoodId().getRecommendFoodId() // 음식 ID만을 보내도록 보장
                        : null
        );
    }

    //음식 평점 조회
    public List<ReadReplyFoodByIdResponse> getRepliesByFoodId(Long recommendFoodId) {
        // 요청에서 제공된 음식 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendFoodId_RecommendFoodId(recommendFoodId);

        // 조회된 후기들을 ReadReplyFoodByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyFoodByIdResponse(
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(), // JSON을 List<String>로 변환
                        reply.getRecommendFoodId() != null ? reply.getRecommendFoodId().getRecommendFoodId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

//    public List<ReadReplyFoodByIdResponse> getRepliesByFoodId(ReadReplyFoodByIdResponse response,Long recommendFoodId) {
//        // 요청에서 제공된 음식 ID를 기반으로 기존의 추천 후기를 가져옵니다.
//        RecommendReply recommendReply = recommendReplyRepository.findById(response.getRecommendFoodId())
//                .orElseThrow(() -> new IllegalArgumentException("음식점 평점 정보 없음"));
//
//        // RecommendListFood의 실제 ID를 사용하여 모든 후기를 조회합니다.
//        RecommendListFood recommendListFood = recommendListFoodRepository.findByRecommendFoodId(recommendFoodId);
//        recommendReply.setRecommendFoodId(recommendListFood); // RecommendListFood 엔티티 설정
//
//        // 수정된 엔티티를 저장합니다.
//        recommendReply = recommendReplyRepository.save(recommendReply);
//
//    return new List<ReadReplyFoodByIdResponse>(
//            recommendReply.getRecommendReplyStar(),
//            recommendReply.getRecommendReplyTagValue() != null
//                    ? recommendReply.getRecommendReplyTagValue() // JSON을 List<String>로 변환
//                    : List.of(),
//            recommendReply.getRecommendFoodId() != null
//                    ? recommendReply.getRecommendFoodId().getRecommendFoodId() // 음식 ID만을 보내도록 보장
//                    : null);
//    }


    // 후기 전체 조회
    @Transactional(readOnly = true)
    public List<ReadRecommendReplyAllResponse> getReplyAll() {
        return recommendReplyRepository.findAll().stream()
                .map(recommendReply -> modelMapper.map(recommendReply, ReadRecommendReplyAllResponse.class))
                .collect(Collectors.toList());
    }

    //food 조회
    @Transactional(readOnly = true)
    public List<ReadReplyFoodAllResponse> getReplyFoods() {
        return recommendReplyRepository.findAll().stream()
                .map(recommendReply -> modelMapper.map(recommendReply, ReadReplyFoodAllResponse.class))
                .collect(Collectors.toList());
    }


    //음식점 평점 Id 1건 조회
//    @Transactional(readOnly = true)
//    public ReadReplyFoodAllResponse getReplyFoodById(Long recommendFoodId) {
//        RecommendReply foodReply = recommendReplyRepository.findById(recommendFoodId)
//                .orElseThrow(() -> new EntityNotFoundException("음식 추천 ID를 찾을 수 없습니다: " + recommendFoodId));
//
//        RecommendListFood recommendFood = foodReply.getRecommendFoodId();
//
//        return new ReadReplyFoodAllResponse(
//                foodReply.getRecommendReplyId(),
//                foodReply.getRecommendReply(),
//                foodReply.getRecommendReplyStar(),
//                foodReply.getRecommendReplyTagValue(),
//                recommendFood != null ? recommendFood.getRecommendFoodId() : null // food 객체가 null이 아니면 ID 반환
//        );
//    }

//    @Transactional(readOnly = true)
//    public List<ReadReplyFoodAllResponse> getReplyFoodByFoodId(Long recommendFoodId) {
//        List<RecommendReply> foodReplies = recommendReplyRepository.findByRecommendFoodId_Id(recommendFoodId);
//
//        return foodReplies.stream().map(foodReply -> new ReadReplyFoodAllResponse(
//                foodReply.getRecommendReplyId(),
//                foodReply.getRecommendReply(),
//                foodReply.getRecommendReplyStar(),
//                foodReply.getRecommendReplyTagValue(),
//                foodReply.getRecommendFoodId() != null ? foodReply.getRecommendFoodId().getRecommendFoodId() : null
//        )).collect(Collectors.toList());
//    }

}

