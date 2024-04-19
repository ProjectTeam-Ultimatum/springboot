package ultimatum.project.controller.recommend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.dto.place.RecommendListPlaceByIdResponse;
import ultimatum.project.domain.dto.recommendReply.*;
import ultimatum.project.domain.dto.reviewDTO.CreateReviewRequest;
import ultimatum.project.domain.dto.reviewDTO.CreateReviewResponse;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.service.recommned.RecommendReplyService;

import java.util.List;

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

    // 새로운 후기 저장
//    @PostMapping
//    public ResponseEntity<RecommendReplyResponse> saveReply(@RequestBody RecommendReplyResponse recommendReplyResponse) {
//        RecommendReplyResponse savedReply = recommendReplyService.saveReply(recommendReplyResponse);
//        return ResponseEntity.ok(savedReply);
//    }

//    @PostMapping("/saveRecommendReply")
//    @Operation(summary = "평점 작성", description = "입력 항목")
//    public ResponseEntity<RecommendReplyResponse> saveRecommendReply(
//        @RequestParam("recommendReplyId") Long recommendReplyId,
//        @RequestParam("recommendReply") String recommendReply,
//        @RequestParam("recommendReplyStar") Long recommendReplyStar,
//        @RequestParam("recommendReplyTagValue") List<String> recommendReplyTagValue,
//        @RequestParam("recommendPlaceId") RecommendListPlace recommendPlaceId,
//        @RequestParam("recommendFoodId") RecommendListFood recommendFoodId,
//        @RequestParam("recommendHotelId") RecommendListHotel recommendHotelId,
//        @RequestParam("recommendEventId") RecommendListEvent recommendEventId) {
//
//        // Create the DTO from the request parameters
//        RecommendReplyResponse recommendReplyResponse = new RecommendReplyResponse(
//                recommendReplyId, recommendReply, recommendReplyStar, recommendReplyTagValue,
//                recommendPlaceId, recommendFoodId, recommendHotelId, recommendEventId);
//
//        // Assuming there's a service method to save this response
//        RecommendReplyResponse savedReply = recommendReplyService.saveReply(recommendReplyResponse);
//
//        return ResponseEntity.ok(savedReply);
//    }

//    @PostMapping("/saveRecommendReply")
//    @Operation(summary = "평점 작성", description = "입력 항목")
//    public ResponseEntity<RecommendReplyResponse> saveRecommendReply(
//            @RequestParam("recommendReplyId") Long recommendReplyId,
//            @RequestParam("recommendReply") String recommendReply,
//            @RequestParam("recommendReplyStar") Long recommendReplyStar,
//            @RequestParam("recommendReplyTagValue") List<String> recommendReplyTagValue,
//            @RequestParam("recommendPlaceId") RecommendListPlace recommendPlaceId,
//            @RequestParam("recommendFoodId") RecommendListFood recommendFoodId,
//            @RequestParam("recommendHotelId") RecommendListHotel recommendHotelId,
//            @RequestParam("recommendEventId") RecommendListEvent recommendEventId) {
//
//
//        // RecommendReplyRequest 객체를 생성하고 파라미터로 전달받은 값을 설정합니다.
//        RecommendReplyRequest request = new RecommendReplyRequest(
//                recommendReplyId, recommendReplyStar, recommendReplyTagValue,
//                recommendPlaceId, recommendFoodId, recommendHotelId, recommendEventId);
//
//        // 서비스를 호출하여 리뷰를 생성하고 응답 객체를 반환합니다.
//        RecommendReplyResponse response = recommendReplyService.saveReply(request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

    //음식점 평점 작성
    @PostMapping("/saveFoodReply")
    @Operation(summary = "음식점 평점 작성", description = "입력 항목")
    public ResponseEntity<CreateReplyFoodResponse> saveRecommendFoodReply(
            @RequestParam("recommendReplyStar") Long recommendReplyStar,
            @RequestParam("recommendReplyTagValue") List<String> recommendReplyTagValue,
            @RequestParam("recommendFoodId") Long recommendFoodId) {

        // RecommendReplyRequest 객체를 생성하고 파라미터로 전달받은 값을 설정합니다.
        CreateReplyFoodRequest request = new CreateReplyFoodRequest(
                recommendReplyStar, recommendReplyTagValue, recommendFoodId);

        // 서비스를 호출하여 리뷰를 생성하고 응답 객체를 반환합니다.
        CreateReplyFoodResponse response = recommendReplyService.createFoodReply(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //음식점 평점 조회
    @GetMapping("/{recommend_food_id}")
    @Operation(summary = "음식점 평점 조회")
    public ResponseEntity<List<ReadReplyFoodByIdResponse>> getFoodReplies(@PathVariable Long recommend_food_id) {
        List<ReadReplyFoodByIdResponse> responses = recommendReplyService.getRepliesByFoodId(recommend_food_id);
        if (responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responses);
    }

    // 모든 후기 조회
    @GetMapping
    @Operation(summary = "평점 전체 조회")
    public ResponseEntity<List<ReadRecommendReplyAllResponse>> getFoodIdReplies() {
        List<ReadRecommendReplyAllResponse> allReplies = recommendReplyService.getReplyAll();
        return ResponseEntity.ok(allReplies);
    }

    //음식점 평점 1개 조회
//    @GetMapping("/food/{recommend_food_id}")
//    @Operation(summary = "음식점평점 id 조회")
//    public ResponseEntity<ReadReplyFoodAllResponse> getByIdListPlaces(@PathVariable Long recommend_food_id) {
//        ReadReplyFoodAllResponse foodAllResponse = recommendReplyService.getReplyFoodById(recommend_food_id);
//        return ResponseEntity.ok(foodAllResponse);
//    }

//    @GetMapping("/food/{recommendFoodId}")
//    @Operation(summary = "음식점 평점 리스트 조회", description = "음식점 ID에 대한 모든 평점을 조회합니다.")
//    public ResponseEntity<List<ReadReplyFoodAllResponse>> getFoodRepliesByFoodId(@PathVariable Long recommendFoodId) {
//        List<ReadReplyFoodAllResponse> responses = recommendReplyService.getReplyFoodByFoodId(recommendFoodId);
//        return ResponseEntity.ok(responses);
//    }

    //food 조회
//    @GetMapping
//    public ResponseEntity<List<ReadReplyFoodAllResponse>> getAllFoodReplies() {
//        List<ReadReplyFoodAllResponse> foodReplies = recommendReplyService.getReplyFoods();
//        return ResponseEntity.ok(foodReplies);
//    }


}
