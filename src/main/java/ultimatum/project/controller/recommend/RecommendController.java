package ultimatum.project.controller.recommend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.event.RecommendListEventByIdResponse;
import ultimatum.project.domain.dto.event.RecommendListEventResponse;
import ultimatum.project.domain.dto.food.RecommendListFoodByIdResponse;
import ultimatum.project.domain.dto.food.RecommendListFoodResponse;
import ultimatum.project.domain.dto.hotel.RecommendListHotelByIdResponse;
import ultimatum.project.domain.dto.hotel.RecommendListHotelResponse;
import ultimatum.project.domain.dto.place.RecommendListPlaceByIdResponse;
import ultimatum.project.domain.dto.place.RecommendListPlaceResponse;
import ultimatum.project.service.recommned.RecommendService;


@Tag(name = "recommend", description = "추천정보")
@Log4j2
@CrossOrigin(origins = "*") // 클라이언트 호스트 주소
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    //Service 생성자 주입
    private final RecommendService recommendService;

    // 생성자
    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    //음식점 전체 조회
    @Tag(name = "recommend", description = "API 맛집 리스트")
    @GetMapping("/listfood")
    public ResponseEntity<Page<RecommendListFoodResponse>> getAllListFoods(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            @PageableDefault(size = 12, sort = "recommendFoodId", direction = Sort.Direction.DESC) Pageable pageable) {

        // Use the integrated service method that handles both cases
        Page<RecommendListFoodResponse> response = recommendService.findRecommendListFood(tag, region, pageable);

        log.info("listfood pageable : {}", pageable);
        return ResponseEntity.ok(response);
    }

    //음식점 1개 조회
    @GetMapping("/listfood/{recommend_food_id}")
    @Operation(summary = "음식점 id 조회")
    public ResponseEntity<RecommendListFoodByIdResponse> getByIdListFoods(@PathVariable Long recommend_food_id) {
        RecommendListFoodByIdResponse response = recommendService.getRecommendListFoodById(recommend_food_id);
        return ResponseEntity.ok(response);
    }

    //관광지 전체 조회
    @Tag(name = "recommend", description = "API 관광지 리스트")
    @GetMapping("/listplace")
    public ResponseEntity<Page<RecommendListPlaceResponse>> getAllListPlaces(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            @PageableDefault(size = 12, sort = "recommendPlaceId", direction = Sort.Direction.DESC) Pageable pageable) {

        // Use the integrated service method that handles both cases
        Page<RecommendListPlaceResponse> response = recommendService.findRecommendListPlace(tag, region, pageable);

        log.info("listplace pageable : {}", pageable);
        return ResponseEntity.ok(response);
    }

    //관광지 1개 조회
    @GetMapping("/listplace/{recommend_place_id}")
    @Operation(summary = "관광지 id 조회")
    public ResponseEntity<RecommendListPlaceByIdResponse> getByIdListPlaces(@PathVariable Long recommend_place_id) {
        RecommendListPlaceByIdResponse response = recommendService.getRecommendListPlaceById(recommend_place_id);
        return ResponseEntity.ok(response);
    }

    //숙박 전체 조회
    @Tag(name = "recommend", description = "API 호텔리스트")
    @GetMapping("/listhotel")
    public ResponseEntity<Page<RecommendListHotelResponse>> getAllListHotels(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            @PageableDefault(size = 12, sort = "recommendHotelId", direction = Sort.Direction.DESC) Pageable pageable) {

        // Use the integrated service method that handles both cases
        Page<RecommendListHotelResponse> response = recommendService.findRecommendListHotel(tag, region, pageable);

        log.info("listhotel pageable : {}", pageable);
        return ResponseEntity.ok(response);
    }

    //숙박 1개 조회
    @GetMapping("/listhotel/{recommend_hotel_id}")
    @Operation(summary = "숙박 id 조회")
    public ResponseEntity<RecommendListHotelByIdResponse> getByIdListHotels(@PathVariable Long recommend_hotel_id) {
        RecommendListHotelByIdResponse response = recommendService.getRecommendListHotelById(recommend_hotel_id);
        return ResponseEntity.ok(response);
    }


    // 축제행사 전체 조회
    @Tag(name = "recommend", description = "API 축제행사리스트")
    @GetMapping("/listevent")
    public ResponseEntity<Page<RecommendListEventResponse>> getAllListEvents(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            @PageableDefault(size = 12, sort = "recommendEventId", direction = Sort.Direction.DESC) Pageable pageable) {

        // Use the integrated service method that handles both cases
        Page<RecommendListEventResponse> response = recommendService.findRecommendListEvent(tag, region, pageable);

        log.info("listhotel pageable : {}", pageable);
        return ResponseEntity.ok(response);
    }

    //축제행사 1개 조회
    @GetMapping("/listevent/{recommend_event_id}")
    @Operation(summary = "축제행사 id 조회")
    public ResponseEntity<RecommendListEventByIdResponse> getByIdListEvents(@PathVariable Long recommend_event_id) {
        RecommendListEventByIdResponse response = recommendService.getRecommendListEventById(recommend_event_id);
        return ResponseEntity.ok(response);
    }


}
