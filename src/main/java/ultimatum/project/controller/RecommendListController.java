package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
//import org.aspectj.bridge.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.dto.food.RecommendListFoodResponse;
import ultimatum.project.dto.image.RecommendImageFoodResponse;
import ultimatum.project.service.RecommendListService;

import java.util.List;


//@Builder
//@Getter
//@Setter
//@ToString
@Tag(name = "recommendList", description = "추천리스트")
@Log4j2
//@Controller //빈 스캐닝
//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recommend")
public class RecommendListController {

    //Service 생성자 주입
    private final RecommendListService recommendListService;

    // 생성자
    public RecommendListController(RecommendListService recommendListService) {
        this.recommendListService = recommendListService;
    }

//    @GetMapping("list")
//    public List<RecommendListDTO> findRecommendList(Model model){
//        List<RecommendListDTO> recommendList = recommendListService.findRecommendList();
//        //model.addAttribute("recommendList", recommendList);
//        log.info("recommendList 는 {}", recommendList);
//        return recommendList;
//    }

    //food 리스트 조회
    @GetMapping("list")
    public ResponseEntity<Page<RecommendListFoodResponse>> listReadAll(
            @PageableDefault(size = 12, sort = "recommendFoodId",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecommendListFoodResponse> responses = recommendListService.readFoodAllList(pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }







}
