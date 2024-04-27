package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.event.RecommendListEventResponse;
import ultimatum.project.domain.dto.food.RecommendListFoodResponse;
import ultimatum.project.domain.dto.hotel.RecommendListHotelResponse;
import ultimatum.project.domain.dto.place.RecommendListPlaceResponse;
import ultimatum.project.service.RecommendService2;

import java.util.List;

@Tag(name = "recommend", description = "추천리스트")
@Log4j2
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recommend")
public class RecommendController2 {

    private final RecommendService2 recommendService;

    public RecommendController2(RecommendService2 recommendService) {
        this.recommendService = recommendService;
    }

    @GetMapping("/list/place")
    public ResponseEntity<Page<RecommendListPlaceResponse>> getListPlaces(Pageable pageable) {
        return ResponseEntity.ok(recommendService.findRecommendListPlace(pageable));
    }

    @GetMapping("/list/food")
    public ResponseEntity<Page<RecommendListFoodResponse>> getListFoods(Pageable pageable) {
        return ResponseEntity.ok(recommendService.findRecommendListFood(pageable));
    }

    @GetMapping("/list/hotel")
    public ResponseEntity<Page<RecommendListHotelResponse>> getListHotels(Pageable pageable) {
        return ResponseEntity.ok(recommendService.findRecommendListHotel(pageable));
    }

    @GetMapping("/list/event")
    public ResponseEntity<Page<RecommendListEventResponse>> getListEvents(Pageable pageable) {
        return ResponseEntity.ok(recommendService.findRecommendListEvent(pageable));
    }

    @GetMapping("/listall")
    public ResponseEntity<Page<Object>> listAll(Pageable pageable) {
        return ResponseEntity.ok(recommendService.findAllRecommendations(pageable));
    }

    @GetMapping("/search/food")
    public ResponseEntity<List<RecommendListFoodResponse>> searchFood(@RequestParam String title, Pageable pageable) {
        List<RecommendListFoodResponse> food = recommendService.searchFoodsByTitle(title, pageable);
        return ResponseEntity.ok(food);
    }

    @GetMapping("/search/place")
    public ResponseEntity<List<RecommendListPlaceResponse>> searchPlace(@RequestParam String title, Pageable pageable) {
        List<RecommendListPlaceResponse> place = recommendService.searchPlacesByTitle(title, pageable);
        return ResponseEntity.ok(place);
    }

    @GetMapping("/search/event")
    public ResponseEntity<List<RecommendListEventResponse>> searchEvent(@RequestParam String title, Pageable pageable) {
        List<RecommendListEventResponse> event = recommendService.searchEventsByTitle(title, pageable);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/search/hotel")
    public ResponseEntity<List<RecommendListHotelResponse>> searchHotel(@RequestParam String title, Pageable pageable) {
        List<RecommendListHotelResponse> hotel = recommendService.searchHotelsByTitle(title, pageable);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<Object>> searchAll(@RequestParam String title, Pageable pageable) {
        List<Object> results = recommendService.searchAllCategoriesByTitle(title, pageable);
        return ResponseEntity.ok(results);
    }
}
