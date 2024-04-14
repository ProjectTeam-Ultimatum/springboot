package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.dto.food.RecommendFoodResponse;
import ultimatum.project.dto.food.RecommendListFoodResponse;
import ultimatum.project.dto.hotel.RecommendHotelResponse;
import ultimatum.project.dto.place.RecommendPlaceResponse;
import ultimatum.project.service.RecommendService;


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

    //food 조회
    @Tag(name = "recommend", description = "맛집리스트")
    //food 조회
    @GetMapping("food")
    public ResponseEntity<Page<RecommendFoodResponse>> foodAll(
            @PageableDefault(size = 12, sort = "recommendFoodId",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecommendFoodResponse> responses = recommendService.readFoodAll(pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    //food list 조회
    @Tag(name = "recommend", description = "API 맛집리스트")
    @GetMapping("/listfood")
    public ResponseEntity<Page<RecommendListFoodResponse>> getListFoods(
            @PageableDefault(size = 12, sort = "recommendFoodId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecommendListFoodResponse> response = recommendService.findRecommendListFood(pageable);
        return ResponseEntity.ok(response);
    }

    //hotel 조회
    @Tag(name = "recommend", description = "숙소리스트")
    @GetMapping("hotel")
    public ResponseEntity<Page<RecommendHotelResponse>> hotelAll(
            @PageableDefault(size = 12, sort = "recommendHotelId",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecommendHotelResponse> responses = recommendService.readHotelAll(pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    //place 조회
    @Tag(name = "recommend", description = "가볼만한 곳 리스트")
    @GetMapping("place")
    public ResponseEntity<Page<RecommendPlaceResponse>> placeAll(
            @PageableDefault(size = 12, sort = "recommendPlaceId",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecommendPlaceResponse> responses = recommendService.readPlaceAll(pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
