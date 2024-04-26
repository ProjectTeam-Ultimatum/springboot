package ultimatum.project.service.recommned;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.recommendReply.*;
import ultimatum.project.domain.dto.recommendReply.event.*;
import ultimatum.project.domain.dto.recommendReply.food.*;
import ultimatum.project.domain.dto.recommendReply.hotel.*;
import ultimatum.project.domain.dto.recommendReply.place.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;
import ultimatum.project.global.config.util.JsonUtil;
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.hotel.RecommendListHotelRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.repository.reply.RecommendReplyRepository;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // 요청에서 제공된 음식 ID를 기반으로 RecommendListFood를 가져옵니다.
        RecommendListFood recommendFood = recommendListFoodRepository.findByRecommendFoodId(request.getRecommendFoodId());
        if (recommendFood == null) {
            log.error("RecommendListFood with ID {} not found.", request.getRecommendFoodId());
            throw new IllegalArgumentException("RecommendListFood 정보 없음");
        }

        // 평점 값이 1에서 5 사이인지 검증합니다.
        long recommendReplyStar = Math.toIntExact(request.getRecommendReplyStar());
        if (recommendReplyStar < 1 || recommendReplyStar > 5) {
            //throw new IllegalArgumentException("The recommend reply star must be between 1 and 5.");
            log.error("------------ 음식 평점을 잘못 입력했습니다 --------------- ");
            log.error("현재 평점 값: {} / 평점은 1에서 5 사이여야 합니다.", recommendReplyStar);
            return null; // 또는 적절한 예외 처리 및 에러 응답 반환
        }

        // Builder 패턴을 사용하여 RecommendReply 객체를 생성합니다.
        RecommendReply recommendReply = RecommendReply.builder()
                .recommendReplyStar(recommendReplyStar)
                .recommendReplyTagValue(request.getRecommendReplyTagValue().toString()) //string, List<string> 타입 맞춤
                .recommendFoodId(recommendFood)
                .build();

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
        // 요청된 관광지 ID를 이용하여 RecommendListPlace 객체를 조회합니다.
        RecommendListPlace recommendPlace = recommendListPlaceRepository.findByRecommendPlaceId(request.getRecommendPlaceId());
        if (recommendPlace == null) {
            // 찾는 관광지 ID가 없는 경우, 로그를 출력하고 예외를 발생시킵니다.
            log.error("RecommendListPlace ID 찾을 수 없음: {}", request.getRecommendPlaceId());
            throw new IllegalArgumentException("RecommendListPlace 정보 없음");
        }

        // 요청받은 평점이 1에서 5 사이인지 검증합니다.
        long recommendReplyStar = Math.toIntExact(request.getRecommendReplyStar());
        if (recommendReplyStar < 1 || recommendReplyStar > 5) {
            // 평점이 요구 범위를 벗어날 경우, 로그를 출력하고 예외 발생
            log.error("관광지 평점 입력 오류: 현재 평점 값: {} / 평점은 1에서 5 사이여야 합니다.", recommendReplyStar);
            throw new IllegalArgumentException("평점은 1에서 5 사이여야 합니다.");
        }

        // 태그 리스트를 JSON String 변환
        String jsonTags = JsonUtil.toJson(request.getRecommendReplyTagValue());

        // RecommendReply 객체를 Builder 패턴 이용하여 생성
        RecommendReply recommendReply = RecommendReply.builder()
                .recommendReplyStar(recommendReplyStar)
                .recommendReplyTagValue(jsonTags) // DB 저장
                .recommendPlaceId(recommendPlace)
                .build();

        // 생성된 객체를 데이터베이스 저장
        recommendReply = recommendReplyRepository.save(recommendReply);

        // 저장된 태그 정보를 로그 출력
        log.info("저장된 태그: {}", jsonTags);

        // 최종적으로 생성된 RecommendReply 객체를 이용하여 응답 객체를 생성하고 반환합니다.
        return new CreateReplyPlaceResponse(
                recommendReply.getRecommendReplyStar(),
                JsonUtil.fromJson(jsonTags), // JSON 문자열을 다시 리스트로 변환, 태그 데이터를 사용
                recommendPlace != null ? recommendPlace.getRecommendPlaceId() : null
        );
    }

    //숙박 평점 저장
    public CreateReplyHotelResponse createHotelReply(CreateReplyHotelRequest request) {
        // 요청에서 제공된 숙박 ID를 기반으로 RecommendListHotel를 가져옵니다.
        RecommendListHotel recommendHotel = recommendListHotelRepository.findByRecommendHotelId(request.getRecommendHotelId());
        if (recommendHotel == null) {
            log.error("RecommendListHotel ID 찾을수 없음", request.getRecommendHotelId());
            throw new IllegalArgumentException("RecommendListHotel 정보 없음");
        }

        // 평점 값이 1에서 5 사이인지 검증합니다.
        long recommendReplyStar = Math.toIntExact(request.getRecommendReplyStar());
        if (recommendReplyStar < 1 || recommendReplyStar > 5) {
            //throw new IllegalArgumentException("The recommend reply star must be between 1 and 5.");
            log.error("------------ 숙박 평점을 잘못 입력했습니다 --------------- ");
            log.error("현재 평점 값: {} / 평점은 1에서 5 사이여야 합니다.", recommendReplyStar);
            return null; // 또는 적절한 예외 처리 및 에러 응답 반환
        }

        // Builder 패턴을 사용하여 RecommendReply 객체를 생성합니다.
        RecommendReply recommendReply = RecommendReply.builder()
                .recommendReplyStar(recommendReplyStar)
                .recommendReplyTagValue(request.getRecommendReplyTagValue().toString()) //string, List<string> 타입 맞춤
                .recommendHotelId(recommendHotel)
                .build();

        log.info(request.getRecommendReplyTagValue().toString());

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
        // 요청에서 제공된 축제행사 ID를 기반으로 RecommendListEvent를 가져옵니다.
        RecommendListEvent recommendEvent = recommendListEventRepository.findByRecommendEventId(request.getRecommendEventId());
        if (recommendEvent == null) {
            log.error("RecommendListEvent ID 찾을수 없음", request.getRecommendEventId());
            throw new IllegalArgumentException("RecommendListEvent 정보 없음");
        }

        // 평점 값이 1에서 5 사이인지 검증합니다.
        long recommendReplyStar = Math.toIntExact(request.getRecommendReplyStar());
        if (recommendReplyStar < 1 || recommendReplyStar > 5) {
            //throw new IllegalArgumentException("The recommend reply star must be between 1 and 5.");
            log.error("------------ 축제행사 평점을 잘못 입력했습니다 --------------- ");
            log.error("현재 평점 값: {} / 평점은 1에서 5 사이여야 합니다.", recommendReplyStar);
            return null; // 또는 적절한 예외 처리 및 에러 응답 반환
        }

        // Builder 패턴을 사용하여 RecommendReply 객체를 생성합니다.
        RecommendReply recommendReply = RecommendReply.builder()
                .recommendReplyStar(recommendReplyStar)
                .recommendReplyTagValue(request.getRecommendReplyTagValue().toString()) //string, List<string> 타입 맞춤
                .recommendEventId(recommendEvent)
                .build();

        // 수정된 엔티티를 저장합니다.
        recommendReply = recommendReplyRepository.save(recommendReply);

        // 응답 객체를 수동으로 생성합니다.
        return new CreateReplyEventResponse(
                recommendReply.getRecommendReplyStar(),
                recommendReply.getRecommendReplyTagValue() != null
                        ? recommendReply.getRecommendReplyTagValue() // JSON을 List<String>로 저장.
                        : List.of(),
                recommendReply.getRecommendEventId() != null
                        ? recommendReply.getRecommendEventId().getRecommendEventId() // 축제행사 ID만을 보내도록 보장
                        : null
        );
    }


    //음식 평점 ID조회 - 최종
    public List<ReadReplyFoodByIdResponse> getRepliesByFoodId(Long recommendFoodId) {
        // 요청에서 제공된 음식점 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendFoodId_RecommendFoodId(recommendFoodId);

        // 조회된 후기들을 ReadReplyFoodByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyFoodByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendFoodId() != null ? reply.getRecommendFoodId().getRecommendFoodId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //음식 평점 평균 계산
    public int getAverageRatingByFoodId(Long recommendFoodId) {
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendFoodId_RecommendFoodId(recommendFoodId);
        OptionalDouble average = replies.stream()
                .mapToDouble(RecommendReply::getRecommendReplyStar)
                .average();
        return average.isPresent() ? (int) Math.round(average.getAsDouble()) : 0; // 평균 평점을 반올림하여 정수로 반환
    }

    //음식 태그 ID조회
    public List<ReadReplyFoodTagByIdResponse> getRepliesByFoodTagId(Long recommendFoodId) {
        // 요청에서 제공된 음식점 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendFoodId_RecommendFoodId(recommendFoodId);

        // 조회된 후기들을 ReadReplyFoodByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyFoodTagByIdResponse(
                        reply.getRecommendReply(),
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
                        reply.getRecommendPlaceId() != null ? reply.getRecommendPlaceId().getRecommendPlaceId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //관광지 태그 ID 조회
    public List<ReadReplyPlaceTagByIdResponse> getRepliesByPlaceTagId(Long recommendPlaceId) {
        // 요청에서 제공된 관광지 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendPlaceId_RecommendPlaceId(recommendPlaceId);

        // 조회된 후기들을 ReadReplyPlaceByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyPlaceTagByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyTagValue(), // JSON을 List<String>로 변환
                        reply.getRecommendPlaceId() != null ? reply.getRecommendPlaceId().getRecommendPlaceId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //관광지 태그카운트 ID 조회
    public List<Map.Entry<String, Long>> getTagsWithCountsByPlaceId(Long recommendPlaceId) {
        // 지정된 recommendPlaceId에 대한 모든 후기에서 태그 리스트를 추출합니다.
        List<String> tags = recommendReplyRepository.findByRecommendPlaceId_RecommendPlaceId(recommendPlaceId).stream()
                .flatMap(reply -> {
                    List<String> tagList = reply.getRecommendReplyTagValue();
                    if (tagList == null || tagList.isEmpty()) {
                        log.info("tagValue가 비어 있습니다.");
                        return Stream.empty();
                    } else {
                        return tagList.stream();
                    }
                })
                .collect(Collectors.toList());

        // 추출된 각 태그의 등장 빈도수를 계산합니다.
        Map<String, Long> tagFrequency = tags.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 태그 빈도수를 내림차순으로 정렬합니다.
        List<Map.Entry<String, Long>> sortedTagFrequency = tagFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        log.info("태그 카운트: {}", sortedTagFrequency);
        return sortedTagFrequency;
    }

    //관광지 평점 평균 계산
    public int getAverageRatingByPlaceId(Long recommendPlaceId) {
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendPlaceId_RecommendPlaceId(recommendPlaceId);
        OptionalDouble average = replies.stream()
                .mapToDouble(RecommendReply::getRecommendReplyStar)
                .average();
        return average.isPresent() ? (int) Math.round(average.getAsDouble()) : 0; // 평균 평점을 반올림하여 정수로 반환
    }



    //숙박 평점 ID 조회
    public List<ReadReplyHotelByIdResponse> getRepliesByHotelId(Long recommendHotelId) {
        // 요청에서 제공된 숙박 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendHotelId_RecommendHotelId(recommendHotelId);

        // 조회된 후기들을 ReadReplyHotelByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyHotelByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyStar(),
                        reply.getRecommendHotelId() != null ? reply.getRecommendHotelId().getRecommendHotelId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //숙박 태그 ID 조회
    public List<ReadReplyHotelTagByIdResponse> getRepliesByHotelTagId(Long recommendHotelId) {
        // 요청에서 제공된 숙박 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendHotelId_RecommendHotelId(recommendHotelId);

        // 조회된 후기들을 ReadReplyHotelByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyHotelTagByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyTagValue(), // JSON을 List<String>로 변환
                        reply.getRecommendHotelId() != null ? reply.getRecommendHotelId().getRecommendHotelId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //숙박 평점 평균 계산
    public int getAverageRatingByHotelId(Long recommendHotelId) {
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendHotelId_RecommendHotelId(recommendHotelId);
        OptionalDouble average = replies.stream()
                .mapToDouble(RecommendReply::getRecommendReplyStar)
                .average();
        return average.isPresent() ? (int) Math.round(average.getAsDouble()) : 0; // 평균 평점을 반올림하여 정수로 반환
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
                        reply.getRecommendEventId() != null ? reply.getRecommendEventId().getRecommendEventId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //축제행사 평점 ID 조회
    public List<ReadReplyEventTagByIdResponse> getRepliesByEventTagId(Long recommendEventId) {
        // 요청에서 제공된 축제행사 ID를 기반으로 모든 후기를 조회합니다.
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendEventId_RecommendEventId(recommendEventId);

        // 조회된 후기들을 ReadReplyEventByIdResponse DTO로 변환합니다.
        return replies.stream()
                .map(reply -> new ReadReplyEventTagByIdResponse(
                        reply.getRecommendReply(),
                        reply.getRecommendReplyTagValue(), // JSON을 List<String>로 변환
                        reply.getRecommendEventId() != null ? reply.getRecommendEventId().getRecommendEventId() : null // 적절한 ID 접근 방식으로 수정
                ))
                .collect(Collectors.toList());
    }

    //축제행사 평점 평균 계산
    public int getAverageRatingByEventId(Long recommendEventId) {
        List<RecommendReply> replies = recommendReplyRepository.findByRecommendEventId_RecommendEventId(recommendEventId);
        OptionalDouble average = replies.stream()
                .mapToDouble(RecommendReply::getRecommendReplyStar)
                .average();
        return average.isPresent() ? (int) Math.round(average.getAsDouble()) : 0; // 평균 평점을 반올림하여 정수로 반환
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

}

