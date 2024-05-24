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
@RestController //Rest API
@RequestMapping("/api/recommend")
public class RecommendController {

    //Service 생성자 주입
    private final RecommendService recommendService;

    //RecommendService를 생성자 주입 방식으로 받아 필드에 할당.
    //컨트롤러가 RecommendService를 사용하여 비즈니스 로직 처리
    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    //음식점 전체 조회
    @Tag(name = "recommend", description = "API 맛집 리스트")
    @Operation(summary = "음식점 태그, 지역 조회")
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
    @Operation(summary = "관광지 태그, 지역 조회")
    @GetMapping("/listplace") //get 요청
    public ResponseEntity<Page<RecommendListPlaceResponse>> getAllListPlaces(
            //@RequestParam: URL 쿼리 파라미터를 통해 태그(tag)와 지역(region)를 선택적으로 받습니다.
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            //@PageableDefault: 기본 페이징 정보를 설정
            @PageableDefault(size = 12, sort = "recommendPlaceId", direction = Sort.Direction.DESC) Pageable pageable) {

        // findRecommendListFood 메서드를 호출하여 음식점 리스트를 가져오고
        Page<RecommendListPlaceResponse> response = recommendService.findRecommendListPlace(tag, region, pageable);

        log.info("listplace pageable : {}", pageable);
        //ResponseEntity로 감싸 클라이언트에 반환
        return ResponseEntity.ok(response);
    }

    //관광지 1개 조회
    @GetMapping("/listplace/{recommend_place_id}") //get요청
    @Operation(summary = "관광지 id 조회")
    //@PathVariable: URL 경로에서 음식점 ID를 추출하여 서비스 메서드에 전달
    public ResponseEntity<RecommendListPlaceByIdResponse> getByIdListPlaces(@PathVariable Long recommend_place_id) {
        //getRecommendListFoodById 메서드를 호출하여 특정 ID의 음식점 정보를 가져오고
        RecommendListPlaceByIdResponse response = recommendService.getRecommendListPlaceById(recommend_place_id);
        //결과를 ResponseEntity로 감싸 클라이언트에 반환
        return ResponseEntity.ok(response);
    }

    //숙박 전체 조회
    @Tag(name = "recommend", description = "API 호텔리스트")
    @Operation(summary = "관광지 태그, 지역 조회")
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
    @Operation(summary = "축제행사 태그, 지역 조회")
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
