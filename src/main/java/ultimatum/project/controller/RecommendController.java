package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.dto.event.RecommendListEventResponse;
import ultimatum.project.dto.food.RecommendListFoodResponse;
import ultimatum.project.dto.hotel.RecommendListHotelResponse;
import ultimatum.project.dto.place.RecommendListPlaceResponse;
import ultimatum.project.service.recommned.RecommendService;


@Tag(name = "recommend", description = "추천리스트")
@Log4j2
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    //Service 생성자 주입
    private final RecommendService recommendService;

    // 생성자
    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    //food 음식점
//    @Tag(name = "recommend", description = "API 맛집리스트")
//    @GetMapping("/listfood")
//    public ResponseEntity<Page<RecommendListFoodResponse>> getListFoods(
//            @PageableDefault(size = 12, sort = "recommendFoodId", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<RecommendListFoodResponse> response = recommendService.findRecommendListFood(pageable);
//        /* page -> number, size, sort 파라미터가 Pageable 객체에 담긴다. */
//        log.info("listfood pageable : {}", pageable);
//        return ResponseEntity.ok(response);
//    }

    @Tag(name = "recommend", description = "API 맛집 리스트")
    @GetMapping("/listfood")
    public ResponseEntity<Page<RecommendListFoodResponse>> getListFoods(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            @PageableDefault(size = 12, sort = "recommendFoodId", direction = Sort.Direction.DESC) Pageable pageable) {

        // Use the integrated service method that handles both cases
        Page<RecommendListFoodResponse> response = recommendService.findRecommendListFood(tag, region, pageable);

        log.info("listplace pageable : {}", pageable);
        return ResponseEntity.ok(response);
    }


    //place 관광지
//    @Tag(name = "recommend", description = "API 관광지리스트")
//    @GetMapping("/listplace")
//    public ResponseEntity<Page<RecommendListPlaceResponse>> getListPlaces(
//            @PageableDefault(size = 12, sort = "recommendPlaceId", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<RecommendListPlaceResponse> response = recommendService.findRecommendListPlace(pageable);
//        log.info("listplace pageable : {}", pageable);
//        return ResponseEntity.ok(response);
//    }

    //place 관광지
//    @Tag(name = "recommend", description = "API 숙박리스트")
//    @GetMapping("/listplace")
//    public ResponseEntity<Page<RecommendListPlaceResponse>> getListPlaces(
//            @PageableDefault(size = 12, sort = "recommendPlaceId", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<RecommendListPlaceResponse> response = recommendService.findRecommendListPlace(pageable);
//        /* page -> number, size, sort 파라미터가 Pageable 객체에 담긴다. */
//        log.info("listplace pageable : {}", pageable);
//        return ResponseEntity.ok(response);
//    }

    @Tag(name = "recommend", description = "API 관광지 리스트")
    @GetMapping("/listplace")
    public ResponseEntity<Page<RecommendListPlaceResponse>> getListPlaces(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            @PageableDefault(size = 12, sort = "recommendPlaceId", direction = Sort.Direction.DESC) Pageable pageable) {

        // Use the integrated service method that handles both cases
        Page<RecommendListPlaceResponse> response = recommendService.findRecommendListPlace(tag, region, pageable);

        log.info("listplace pageable : {}", pageable);
        return ResponseEntity.ok(response);
    }

    //hotel 숙박
//    @Tag(name = "recommend", description = "API 숙박리스트")
//    @GetMapping("/listhotel")
//    public ResponseEntity<Page<RecommendListHotelResponse>> getListHotels(
//            @PageableDefault(size = 12, sort = "recommendHotelId", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<RecommendListHotelResponse> response = recommendService.findRecommendListHotel(pageable);
//        log.info("listhotel pageable : {}", pageable);
//        return ResponseEntity.ok(response);
//    }

    //hotel 숙박
//    @Tag(name = "recommend", description = "API 숙박리스트")
//    @GetMapping("/listhotel")
//    public ResponseEntity<Page<RecommendListHotelResponse>> getListHotels(
//            @PageableDefault(size = 12, sort = "recommendHotelId", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<RecommendListHotelResponse> response = recommendService.findRecommendListHotel(pageable);
//        /* page -> number, size, sort 파라미터가 Pageable 객체에 담긴다. */
//        log.info("listevent pageable : {}", pageable);
//        return ResponseEntity.ok(response);
//    }

    @Tag(name = "recommend", description = "API 호텔리스트")
    @GetMapping("/listhotel")
    public ResponseEntity<Page<RecommendListHotelResponse>> getListHotels(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            @PageableDefault(size = 12, sort = "recommendHotelId", direction = Sort.Direction.DESC) Pageable pageable) {

        // Use the integrated service method that handles both cases
        Page<RecommendListHotelResponse> response = recommendService.findRecommendListHotel(tag, region, pageable);

        log.info("listhotel pageable : {}", pageable);
        return ResponseEntity.ok(response);
    }
    //event 축제행사
//    @Tag(name = "recommend", description = "API 축제행사리스트")
//    @GetMapping("/listevent")
//    public ResponseEntity<Page<RecommendListEventResponse>> getListEvents(
//            @PageableDefault(size = 12, sort = "recommendEventId", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<RecommendListEventResponse> response = recommendService.findRecommendListEvent(pageable);
//        /* page -> number, size, sort 파라미터가 Pageable 객체에 담긴다. */
//        log.info("listevent pageable : {}", pageable);
//        return ResponseEntity.ok(response);
//    }

//    @Tag(name = "recommend", description = "API 축제행사리스트")
//    @GetMapping("/listevent")
//    public ResponseEntity<Page<RecommendListEventResponse>> getListEvents(
//            @RequestParam(value = "tag", required = false) String tag,
//            @PageableDefault(size = 12, sort = "recommendEventId", direction = Sort.Direction.DESC) Pageable pageable) {
//
//        // Use the integrated service method that handles both cases
//        Page<RecommendListEventResponse> response = recommendService.findRecommendListEvent(tag, pageable);
//
//        log.info("listevent pageable : {}", pageable);
//        return ResponseEntity.ok(response);
//    }

    @Tag(name = "recommend", description = "API 축제행사리스트")
    @GetMapping("/listevent")
    public ResponseEntity<Page<RecommendListEventResponse>> getListEvents(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "region", required = false) String region,
            @PageableDefault(size = 12, sort = "recommendEventId", direction = Sort.Direction.DESC) Pageable pageable) {

        // Use the integrated service method that handles both cases
        Page<RecommendListEventResponse> response = recommendService.findRecommendListEvent(tag, region, pageable);

        log.info("listhotel pageable : {}", pageable);
        return ResponseEntity.ok(response);
    }

}
