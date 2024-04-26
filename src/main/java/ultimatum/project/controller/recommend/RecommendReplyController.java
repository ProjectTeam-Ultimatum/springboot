package ultimatum.project.controller.recommend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.recommendReply.*;
import ultimatum.project.domain.dto.recommendReply.event.*;
import ultimatum.project.domain.dto.recommendReply.food.*;
import ultimatum.project.domain.dto.recommendReply.hotel.*;
import ultimatum.project.domain.dto.recommendReply.place.*;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;
import ultimatum.project.service.recommned.RecommendReplyService;

import java.util.List;
import java.util.Map;

@Tag(name = "recommend", description = "평점정보")
@Log4j2
@CrossOrigin(origins = "*") // 클라이언트 호스트 주소
@RestController
@RequestMapping("/api/recommendreply")
public class RecommendReplyController {

    private final RecommendReplyService recommendReplyService;

    // 생성자
    public RecommendReplyController(RecommendReplyService recommendReplyService) {
        this.recommendReplyService = recommendReplyService;
    }

    //음식점 평점 작성 - 최종
    @PostMapping("/food/save")
    @Operation(summary = "음식점 평점 작성", description = "입력 항목")
    public ResponseEntity<CreateReplyFoodResponse> saveRecommendFoodReply(
            @RequestParam("recommendReplyStar") Long recommendReplyStar,
            @RequestParam("recommendReplyTagValue") List<String> recommendReplyTagValue,
            @RequestParam("recommendFoodId") Long recommendFoodId) {

        // RecommendReplyRequest 객체를 생성하고 파라미터로 전달받은 값을 설정합니다.
        CreateReplyFoodRequest request = new CreateReplyFoodRequest(
                recommendReplyStar, recommendReplyTagValue, recommendFoodId);

        // 서비스를 호출하여 리뷰를 생성하고 응답 객체를 반환합니다.
        //CreateReplyFoodResponse response = recommendReplyService.createFoodReply(request);
        CreateReplyFoodResponse response = recommendReplyService.createFoodReply(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //관광지 평점 작성
    @PostMapping("/place/save")
    @Operation(summary = "관광지 평점 작성", description = "입력 항목")
    public ResponseEntity<CreateReplyPlaceResponse> saveRecommendPlaceReply(
            @RequestParam("recommendReplyStar") Long recommendReplyStar,
            @RequestParam("recommendReplyTagValue") List<String> recommendReplyTagValue,
            @RequestParam("recommendPlaceId") Long recommendPlaceId) {

        // RecommendReplyRequest 객체를 생성하고 파라미터로 전달받은 값을 설정합니다.
        CreateReplyPlaceRequest request = new CreateReplyPlaceRequest(
                recommendReplyStar, recommendReplyTagValue, recommendPlaceId);

        // 서비스를 호출하여 리뷰를 생성하고 응답 객체를 반환합니다.
        CreateReplyPlaceResponse response = recommendReplyService.createPlaceReply(request);
        log.info(response.getRecommendReplyTagValue().toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //숙박 평점 작성
    @PostMapping("/hotel/save")
    @Operation(summary = "숙박 평점 작성", description = "입력 항목")
    public ResponseEntity<CreateReplyHotelResponse> saveRecommendHotelReply(
            @RequestParam("recommendReplyStar") Long recommendReplyStar,
            @RequestParam("recommendReplyTagValue") List<String> recommendReplyTagValue,
            @RequestParam("recommendHotelId") Long recommendHotelId) {

        // RecommendReplyRequest 객체를 생성하고 파라미터로 전달받은 값을 설정합니다.
        CreateReplyHotelRequest request = new CreateReplyHotelRequest(
                recommendReplyStar, recommendReplyTagValue, recommendHotelId);

        // 서비스를 호출하여 리뷰를 생성하고 응답 객체를 반환합니다.
        CreateReplyHotelResponse response = recommendReplyService.createHotelReply(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //축제행사 평점 작성
    @PostMapping("/event/save")
    @Operation(summary = "축제행사 평점 작성", description = "입력 항목")
    public ResponseEntity<CreateReplyEventResponse> saveRecommendEventReply(
            @RequestParam("recommendReplyStar") Long recommendReplyStar,
            @RequestParam("recommendReplyTagValue") List<String> recommendReplyTagValue,
            @RequestParam("recommendEventId") Long recommendEventId) {

        // RecommendReplyRequest 객체를 생성하고 파라미터로 전달받은 값을 설정합니다.
        CreateReplyEventRequest request = new CreateReplyEventRequest(
                recommendReplyStar, recommendReplyTagValue, recommendEventId);

        // 서비스를 호출하여 리뷰를 생성하고 응답 객체를 반환합니다.
        CreateReplyEventResponse response = recommendReplyService.createEventReply(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //음식점 평점 조회 - 최종
    @GetMapping("/food/reads/star/{recommend_food_id}")
    @Operation(summary = "음식점 평점 조회")
    public ResponseEntity<List<ReadReplyFoodByIdResponse>> getFoodReplies(@PathVariable Long recommend_food_id) {
        List<ReadReplyFoodByIdResponse> responses = recommendReplyService.getRepliesByFoodId(recommend_food_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    //음식점 평균 평점 계산
    @GetMapping("/food/average/star/{recommend_food_id}")
    @Operation(summary = "음식점 평균 평점 계산")
    public ResponseEntity<?> getAverageFoodRating(@PathVariable("recommend_food_id") Long recommendFoodId) {
        double averageRating = recommendReplyService.getAverageRatingByFoodId(recommendFoodId);
        if (averageRating == 0.0) {
            // 평균 평점이 없는 경우, 메시지 반환
            return ResponseEntity.ok("평점을 기다리고 있어요");
        }
        return ResponseEntity.ok(averageRating);  // 평균 평점 반환
    }

    //음식점 태그 조회
    @GetMapping("/food/reads/tag/{recommend_food_id}")
    @Operation(summary = "음식점 태그 조회")
    public ResponseEntity<List<ReadReplyFoodTagByIdResponse>> getFoodTagReplies(@PathVariable Long recommend_food_id) {
        List<ReadReplyFoodTagByIdResponse> responses = recommendReplyService.getRepliesByFoodTagId(recommend_food_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    //관광지 평점 조회
    @GetMapping("/place/reads/star/{recommend_place_id}")
    @Operation(summary = "관광지 평점 조회")
    public ResponseEntity<List<ReadReplyPlaceByIdResponse>> getPlaceReplies(@PathVariable Long recommend_place_id) {
        List<ReadReplyPlaceByIdResponse> responses = recommendReplyService.getRepliesByPlaceId(recommend_place_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    //관광지 태그 조회
    @GetMapping("/place/reads/tag/{recommend_place_id}")
    @Operation(summary = "관광지 태그 조회")
    public ResponseEntity<List<ReadReplyPlaceTagByIdResponse>> getPlaceTagReplies(@PathVariable Long recommend_place_id) {
        List<ReadReplyPlaceTagByIdResponse> responses = recommendReplyService.getRepliesByPlaceTagId(recommend_place_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/place/reads/tag-couting/{recommend_place_id}")
    @Operation(summary = "관광지 태그 카운팅")
    public ResponseEntity<List<Map.Entry<String, Long>>> getPlaceTagFrequencies(@PathVariable("recommend_place_id") Long recommendPlaceId) {
        List<Map.Entry<String, Long>> tagFrequencies = recommendReplyService.getTagsWithCountsByPlaceId(recommendPlaceId);
        if (tagFrequencies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tagFrequencies);
    }


    //관광지 평균 평점 계산
    @GetMapping("/place/average/star/{recommend_place_id}")
    @Operation(summary = "관광지 평균 평점 계산")
    public ResponseEntity<?> getAveragePlaceRating(@PathVariable("recommend_place_id") Long recommendPlaceId) {
        double averageRating = recommendReplyService.getAverageRatingByPlaceId(recommendPlaceId);
        if (averageRating == 0.0) {
            // 평균 평점이 없는 경우, 메시지 반환
            return ResponseEntity.ok("평점을 기다리고 있어요");
        }
        return ResponseEntity.ok(averageRating);  // 평균 평점 반환
    }


    //숙박 평점 조회
    @GetMapping("/hotel/reads/star/{recommend_hotel_id}")
    @Operation(summary = "숙박 평점 조회")
    public ResponseEntity<List<ReadReplyHotelByIdResponse>> getHotelReplies(@PathVariable Long recommend_hotel_id) {
        List<ReadReplyHotelByIdResponse> responses = recommendReplyService.getRepliesByHotelId(recommend_hotel_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    //숙박 태그 조회
    @GetMapping("/hotel/reads/tag/{recommend_hotel_id}")
    @Operation(summary = "숙박 태그 조회")
    public ResponseEntity<List<ReadReplyHotelTagByIdResponse>> getHotelTagReplies(@PathVariable Long recommend_hotel_id) {
        List<ReadReplyHotelTagByIdResponse> responses = recommendReplyService.getRepliesByHotelTagId(recommend_hotel_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    //숙박 평균 평점 계산
    @GetMapping("/hotel/average/star/{recommend_hotel_id}")
    @Operation(summary = "숙박 평균 평점 계산")
    public ResponseEntity<?> getAverageHotelRating(@PathVariable("recommend_hotel_id") Long recommendHotelId) {
        double averageRating = recommendReplyService.getAverageRatingByHotelId(recommendHotelId);
        if (averageRating == 0.0) {
            // 평균 평점이 없는 경우, 메시지 반환
            return ResponseEntity.ok("평점을 기다리고 있어요");
        }
        return ResponseEntity.ok(averageRating);  // 평균 평점 반환
    }

    //축제행사 평점 조회
    @GetMapping("/event/reads/star/{recommend_event_id}")
    @Operation(summary = "축제행사 평점 조회")
    public ResponseEntity<List<ReadReplyEventByIdResponse>> getEventReplies(@PathVariable Long recommend_event_id) {
        List<ReadReplyEventByIdResponse> responses = recommendReplyService.getRepliesByEventId(recommend_event_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    //축제행사 태그 조회
    @GetMapping("/event/reads/tag/{recommend_event_id}")
    @Operation(summary = "축제행사 태그 조회")
    public ResponseEntity<List<ReadReplyEventTagByIdResponse>> getEventTagReplies(@PathVariable Long recommend_event_id) {
        List<ReadReplyEventTagByIdResponse> responses = recommendReplyService.getRepliesByEventTagId(recommend_event_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    //축제행사 평균 평점 계산
    @GetMapping("/event/average/star/{recommend_event_id}")
    @Operation(summary = "축제행사 평균 평점 계산")
    public ResponseEntity<?> getAverageEventRating(@PathVariable("recommend_event_id") Long recommendEventId) {
        double averageRating = recommendReplyService.getAverageRatingByEventId(recommendEventId);
        if (averageRating == 0.0) {
            // 평균 평점이 없는 경우, 메시지 반환
            return ResponseEntity.ok("평점을 기다리고 있어요");
        }
        return ResponseEntity.ok(averageRating);  // 평균 평점 반환
    }
    

    // 태그 기반 음식점 조회
    @Operation(summary = "음식점 평점 태그 필터링 조회")
    @GetMapping("/food/reads/all-tag")
    public ResponseEntity<List<ReadReplyFoodAllResponse>> getAllRepliesByTag(
            @RequestParam(value = "recommendReplyTagValue", required = false) String recommendReplyTagValue) {

        List<ReadReplyFoodAllResponse> responses = recommendReplyService.findAllReplyFoodTag(recommendReplyTagValue, Pageable.unpaged());
        return ResponseEntity.ok(responses);
    }

    // 태그 기반 관광지 조회
    @Operation(summary = "관광지 평점 태그 필터링 조회")
    @GetMapping("/place/all/all-tag")
    public ResponseEntity<List<ReadReplyPlaceAllResponse>> findAllReplyPlaceTag(
            @RequestParam(value = "recommendReplyTagValue", required = false) String recommendReplyTagValue) {

        List<ReadReplyPlaceAllResponse> responses = recommendReplyService.findAllReplyPlaceTag(recommendReplyTagValue, Pageable.unpaged());
        return ResponseEntity.ok(responses);
    }

    // 태그 기반 숙박 조회
    @Operation(summary = "숙박 평점 태그 필터링 조회")
    @GetMapping("/hotel/read/all-tag")
    public ResponseEntity<List<ReadReplyHotelAllResponse>> findAllReplyHotelTag(
            @RequestParam(value = "recommendReplyTagValue", required = false) String recommendReplyTagValue) {

        List<ReadReplyHotelAllResponse> responses = recommendReplyService.findAllReplyHotelTag(recommendReplyTagValue, Pageable.unpaged());
        return ResponseEntity.ok(responses);
    }

    // 태그 기반 축제행사 조회
    @Operation(summary = "축제행사 평점 태그 필터링 조회")
    @GetMapping("/event/read/all-tag")
    public ResponseEntity<List<ReadReplyEventAllResponse>> findAllReplyEventTag(
            @RequestParam(value = "recommendReplyTagValue", required = false) String recommendReplyTagValue) {

        List<ReadReplyEventAllResponse> responses = recommendReplyService.findAllReplyEventTag(recommendReplyTagValue, Pageable.unpaged());
        return ResponseEntity.ok(responses);
    }


    // 모든 후기 조회
    @GetMapping
    @Operation(summary = "평점 전체 조회")
    public ResponseEntity<List<ReadRecommendReplyAllResponse>> getFoodIdReplies() {
        List<ReadRecommendReplyAllResponse> allReplies = recommendReplyService.getReplyAll();
        return ResponseEntity.ok(allReplies);
    }


}
