package ultimatum.project.service.recommned;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.recommendReply.*;
import ultimatum.project.domain.dto.recommendReply.event.CreateReplyEventRequest;
import ultimatum.project.domain.dto.recommendReply.event.CreateReplyEventResponse;
import ultimatum.project.domain.dto.recommendReply.event.ReadReplyEventAllResponse;
import ultimatum.project.domain.dto.recommendReply.event.ReadReplyEventByIdResponse;
import ultimatum.project.domain.dto.recommendReply.food.CreateReplyFoodRequest;
import ultimatum.project.domain.dto.recommendReply.food.CreateReplyFoodResponse;
import ultimatum.project.domain.dto.recommendReply.food.ReadReplyFoodAllResponse;
import ultimatum.project.domain.dto.recommendReply.food.ReadReplyFoodByIdResponse;
import ultimatum.project.domain.dto.recommendReply.hotel.CreateReplyHotelRequest;
import ultimatum.project.domain.dto.recommendReply.hotel.CreateReplyHotelResponse;
import ultimatum.project.domain.dto.recommendReply.hotel.ReadReplyHotelAllResponse;
import ultimatum.project.domain.dto.recommendReply.hotel.ReadReplyHotelByIdResponse;
import ultimatum.project.domain.dto.recommendReply.place.CreateReplyPlaceRequest;
import ultimatum.project.domain.dto.recommendReply.place.CreateReplyPlaceResponse;
import ultimatum.project.domain.dto.recommendReply.place.ReadReplyPlaceAllResponse;
import ultimatum.project.domain.dto.recommendReply.place.ReadReplyPlaceByIdResponse;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;
import ultimatum.project.repository.event.RecommendListEventRepository;
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
    private RecommendListPlaceRepository recommendListPlaceRepository;
    private RecommendListHotelRepository recommendListHotelRepository;
    private RecommendListEventRepository recommendListEventRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void RecommendReplyService(RecommendReplyRepository recommendReplyRepository,
                                      RecommendListFoodRepository recommendListFoodRepository,
                                      RecommendListPlaceRepository recommendListPlaceRepository,
                                      RecommendListHotelRepository recommendListHotelRepository,
                                      RecommendListEventRepository recommendListEventRepository,
                                      ModelMapper modelMapper) {
        this.recommendReplyRepository = recommendReplyRepository;
        this.recommendListFoodRepository = recommendListFoodRepository;
        this.recommendListPlaceRepository = recommendListPlaceRepository;
        this.recommendListHotelRepository = recommendListHotelRepository;
        this.recommendListEventRepository = recommendListEventRepository;
        this.modelMapper = modelMapper;
    }

    //음식 평점 저장 - 최종
    public CreateReplyFoodResponse createFoodReply(CreateReplyFoodRequest request) {
        // 요청에서 제공된 음식 ID를 기반으로 기존의 추천 후기를 가져옵니다.
        RecommendReply recommendReply = recommendReplyRepository.findById(request.getRecommendFoodId())
                .orElseThrow(() -> new IllegalArgumentException("음식점 평점 정보 없음"));

        // 평점 값이 1에서 5 사이인지 검증합니다.
        int recommendReplyStar = Math.toIntExact(request.getRecommendReplyStar());
        if (recommendReplyStar < 1 || recommendReplyStar > 5) {
            //throw new IllegalArgumentException("The recommend reply star must be between 1 and 5.");
            log.error("------------ 음식 평점을 잘못 입력했습니다 --------------- ");
            log.error("현재 평점 값: {} / 평점은 1에서 5 사이여야 합니다.", recommendReplyStar);
            return null; // 또는 적절한 예외 처리 및 에러 응답 반환
        }

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

    //관광지 평점 저장
    public CreateReplyPlaceResponse createPlaceReply(CreateReplyPlaceRequest request) {
        // 요청에서 제공된 관광지 ID를 기반으로 기존의 추천 후기를 가져옵니다.
        RecommendReply recommendReply = recommendReplyRepository.findById(request.getRecommendPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("관광지점 평점 정보 없음"));

        // 평점 값이 1에서 5 사이인지 검증합니다.
        int recommendReplyStar = Math.toIntExact(request.getRecommendReplyStar());
        if (recommendReplyStar < 1 || recommendReplyStar > 5) {
            //throw new IllegalArgumentException("The recommend reply star must be between 1 and 5.");
            log.error("------------ 관광지 평점을 잘못 입력했습니다 --------------- ");
            log.error("현재 평점 값: {} / 평점은 1에서 5 사이여야 합니다.", recommendReplyStar);
            return null; // 또는 적절한 예외 처리 및 에러 응답 반환
        }

        // 요청에서 제공된 관광지 ID를 기반으로 RecommendListPlace를 가져옵니다.
        RecommendListPlace recommendPlace = recommendListPlaceRepository.findByRecommendPlaceId(request.getRecommendPlaceId());

        // 요청 데이터를 기존의 RecommendReply 엔티티로 매핑합니다.
        recommendReply.setRecommendReplyStar(request.getRecommendReplyStar());
        recommendReply.setRecommendReplyTagValue(request.getRecommendReplyTagValue());
        recommendReply.setRecommendPlaceId(recommendPlace); // RecommendListPlace 엔티티 설정

        // 수정된 엔티티를 저장합니다.
        recommendReply = recommendReplyRepository.save(recommendReply);

        // 응답 객체를 수동으로 생성합니다.
        return new CreateReplyPlaceResponse(
                recommendReply.getRecommendReplyStar(),
                recommendReply.getRecommendReplyTagValue() != null
                        ? recommendReply.getRecommendReplyTagValue() // JSON을 List<String>로 변환
                        : List.of(),
                recommendReply.getRecommendPlaceId() != null
                        ? recommendReply.getRecommendPlaceId().getRecommendPlaceId() // 관광지 ID만을 보내도록 보장
                        : null
        );
    }

    //숙박 평점 저장
    public CreateReplyHotelResponse createHotelReply(CreateReplyHotelRequest request) {
        // 요청에서 제공된 숙박 ID를 기반으로 기존의 추천 후기를 가져옵니다.
        RecommendReply recommendReply = recommendReplyRepository.findById(request.getRecommendHotelId())
                .orElseThrow(() -> new IllegalArgumentException("숙박 평점 정보 없음"));

        // 평점 값이 1에서 5 사이인지 검증합니다.
        int recommendReplyStar = Math.toIntExact(request.getRecommendReplyStar());
        if (recommendReplyStar < 1 || recommendReplyStar > 5) {
            //throw new IllegalArgumentException("The recommend reply star must be between 1 and 5.");
            log.error("------------ 숙박 평점을 잘못 입력했습니다 --------------- ");
            log.error("현재 평점 값: {} / 평점은 1에서 5 사이여야 합니다.", recommendReplyStar);
            return null; // 또는 적절한 예외 처리 및 에러 응답 반환
        }

        // 요청에서 제공된 숙박 ID를 기반으로 RecommendListHotel를 가져옵니다.
        RecommendListHotel recommendHotel = recommendListHotelRepository.findByRecommendHotelId(request.getRecommendHotelId());

        // 요청 데이터를 기존의 RecommendReply 엔티티로 매핑합니다.
        recommendReply.setRecommendReplyStar(request.getRecommendReplyStar());
        recommendReply.setRecommendReplyTagValue(request.getRecommendReplyTagValue());
        recommendReply.setRecommendHotelId(recommendHotel); // RecommendListHotel 엔티티 설정

        // 수정된 엔티티를 저장합니다.
        recommendReply = recommendReplyRepository.save(recommendReply);

        // 응답 객체를 수동으로 생성합니다.
        return new CreateReplyHotelResponse(
                recommendReply.getRecommendReplyStar(),
                recommendReply.getRecommendReplyTagValue() != null
                        ? recommendReply.getRecommendReplyTagValue() // JSON을 List<String>로 변환
                        : List.of(),
                recommendReply.getRecommendHotelId() != null
                        ? recommendReply.getRecommendHotelId().getRecommendHotelId() // 숙박 ID만을 보내도록 보장
                        : null
        );
    }

    //축제행사 평점 저장
    public CreateReplyEventResponse createEventReply(CreateReplyEventRequest request) {
        // 요청에서 제공된 축제행사 ID를 기반으로 기존의 추천 후기를 가져옵니다.
        RecommendReply recommendReply = recommendReplyRepository.findById(request.getRecommendEventId())
                .orElseThrow(() -> new IllegalArgumentException("축제행사 평점 정보 없음"));

        // 평점 값이 1에서 5 사이인지 검증합니다.
        int recommendReplyStar = Math.toIntExact(request.getRecommendReplyStar());
        if (recommendReplyStar < 1 || recommendReplyStar > 5) {
            //throw new IllegalArgumentException("The recommend reply star must be between 1 and 5.");
            log.error("------------ 축제행사 평점을 잘못 입력했습니다 --------------- ");
            log.error("현재 평점 값: {} / 평점은 1에서 5 사이여야 합니다.", recommendReplyStar);
            return null; // 또는 적절한 예외 처리 및 에러 응답 반환
        }

        // 요청에서 제공된 축제행사 ID를 기반으로 RecommendListEvent를 가져옵니다.
        RecommendListEvent recommendEvent = recommendListEventRepository.findByRecommendEventId(request.getRecommendEventId());

        // 요청 데이터를 기존의 RecommendReply 엔티티로 매핑합니다.
        recommendReply.setRecommendReplyStar(request.getRecommendReplyStar());
        recommendReply.setRecommendReplyTagValue(request.getRecommendReplyTagValue());
        recommendReply.setRecommendEventId(recommendEvent); // RecommendListEvent 엔티티 설정

        // 수정된 엔티티를 저장합니다.
        recommendReply = recommendReplyRepository.save(recommendReply);

        // 응답 객체를 수동으로 생성합니다.
        return new CreateReplyEventResponse(
                recommendReply.getRecommendReplyStar(),
                recommendReply.getRecommendReplyTagValue() != null
                        ? recommendReply.getRecommendReplyTagValue() // JSON을 List<String>로 변환
                        : List.of(),
                recommendReply.getRecommendEventId() != null
                        ? recommendReply.getRecommendEventId().getRecommendEventId() // 축제행사 ID만을 보내도록 보장
                        : null
        );
    }


    //음식 평점 ID 조회 - 최종
    public List<ReadReplyFoodByIdResponse> getRepliesByFoodId(Long recommendFoodId) {
        // 요청에서 제공된 음식점 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendFoodId_RecommendFoodId(recommendFoodId);

        // 조회된 후기들을 ReadReplyFoodByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyFoodByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(), // JSON을 List<String>로 변환
                        reply.getRecommendFoodId() != null ? reply.getRecommendFoodId().getRecommendFoodId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //관광지 평점 ID 조회
    public List<ReadReplyPlaceByIdResponse> getRepliesByPlaceId(Long recommendPlaceId) {
        // 요청에서 제공된 관광지 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendPlaceId_RecommendPlaceId(recommendPlaceId);

        // 조회된 후기들을 ReadReplyPlaceByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyPlaceByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(), // JSON을 List<String>로 변환
                        reply.getRecommendPlaceId() != null ? reply.getRecommendPlaceId().getRecommendPlaceId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //관광지 평점 ID 조회
    public List<ReadReplyHotelByIdResponse> getRepliesByHotelId(Long recommendHotelId) {
        // 요청에서 제공된 숙박 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendHotelId_RecommendHotelId(recommendHotelId);

        // 조회된 후기들을 ReadReplyHotelByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyHotelByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(), // JSON을 List<String>로 변환
                        reply.getRecommendHotelId() != null ? reply.getRecommendHotelId().getRecommendHotelId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //축제행사 평점 ID 조회
    public List<ReadReplyEventByIdResponse> getRepliesByEventId(Long recommendEventId) {
        // 요청에서 제공된 축제행사 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendEventId_RecommendEventId(recommendEventId);

        // 조회된 후기들을 ReadReplyEventByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyEventByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(), // JSON을 List<String>로 변환
                        reply.getRecommendEventId() != null ? reply.getRecommendEventId().getRecommendEventId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }


    // 음식점 전체조회 및 태그 필터링
    public List<ReadReplyFoodAllResponse> findAllReplyFoodTag(String recommendReplyTagValue, Pageable pageable) {
        Page<RecommendReply> replies = recommendReplyRepository.findByRecommendReplyTagValueContainingIgnoreCase(recommendReplyTagValue, pageable);

        return replies.stream()
                .map(reply -> new ReadReplyFoodAllResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(),
                        reply.getRecommendFoodId() != null ? reply.getRecommendFoodId().getRecommendFoodId() : null
                ))
                .collect(Collectors.toList());
    }

    // 관광지 전체조회 및 태그 필터링
    public List<ReadReplyPlaceAllResponse> findAllReplyPlaceTag(String recommendReplyTagValue, Pageable pageable) {
        Page<RecommendReply> replies = recommendReplyRepository.findByRecommendReplyTagValueContainingIgnoreCase(recommendReplyTagValue, pageable);

        return replies.stream()
                .map(reply -> new ReadReplyPlaceAllResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(),
                        reply.getRecommendPlaceId() != null ? reply.getRecommendPlaceId().getRecommendPlaceId() : null
                ))
                .collect(Collectors.toList());
    }

    // 숙박 전체조회 및 태그 필터링
    public List<ReadReplyHotelAllResponse> findAllReplyHotelTag(String recommendReplyTagValue, Pageable pageable) {
        Page<RecommendReply> replies = recommendReplyRepository.findByRecommendReplyTagValueContainingIgnoreCase(recommendReplyTagValue, pageable);

        return replies.stream()
                .map(reply -> new ReadReplyHotelAllResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(),
                        reply.getRecommendHotelId() != null ? reply.getRecommendHotelId().getRecommendHotelId() : null
                ))
                .collect(Collectors.toList());
    }

    // 축제행사 전체조회 및 태그 필터링
    public List<ReadReplyEventAllResponse> findAllReplyEventTag(String recommendReplyTagValue, Pageable pageable) {
        Page<RecommendReply> replies = recommendReplyRepository.findByRecommendReplyTagValueContainingIgnoreCase(recommendReplyTagValue, pageable);

        return replies.stream()
                .map(reply -> new ReadReplyEventAllResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendReplyTagValue(),
                        reply.getRecommendEventId() != null ? reply.getRecommendEventId().getRecommendEventId() : null
                ))
                .collect(Collectors.toList());
    }


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

