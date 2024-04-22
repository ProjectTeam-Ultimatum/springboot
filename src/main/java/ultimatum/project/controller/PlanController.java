package ultimatum.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.PlanRequest;
import ultimatum.project.domain.dto.plan.PlanDetailDTO;
import ultimatum.project.domain.dto.plan.PlanEventDTO;
import ultimatum.project.domain.dto.plan.PlanFoodDTO;
import ultimatum.project.domain.dto.plan.PlanPlaceDTO;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.plan.Plan;
import ultimatum.project.domain.entity.plan.PlanEvent;
import ultimatum.project.domain.entity.plan.PlanFood;
import ultimatum.project.domain.entity.plan.PlanPlace;
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.service.PlanService;

import java.time.format.DateTimeParseException;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private PlanService planService;
    @Autowired
    private RecommendListFoodRepository recommendFoodRepository;
    @Autowired
    private RecommendListEventRepository recommendEventRepository;
    @Autowired
    private RecommendListPlaceRepository recommendPlaceRepository;

    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);

    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody PlanRequest planRequest) {
        logger.info("Received plan request: {}", planRequest);

        if (planRequest.getPlanStartDay() == null || planRequest.getPlanEndDay() == null) {
            logger.error("Start and end dates cannot be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start and end dates are required.");
        }

        try {
            Plan plan = planService.createPlan(planRequest);
            logger.info("Plan created successfully with ID: {}", plan.getPlanId());
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            logger.error("Error creating plan: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> handleDateTimeParseExceptions(DateTimeParseException ex) {
        logger.error("날짜 형식 파싱 오류: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("날짜 형식이 잘못되었습니다.");
    }

    // 장소 저장
    @PostMapping("/{category}/add")
    public ResponseEntity<?> addPlanDetail(@PathVariable String category, @RequestBody PlanDetailDTO planDetailDTO) {
        try {
            switch (category) {
                case "food":
                    RecommendListFood recommendFood = recommendFoodRepository.findById(planDetailDTO.getRecommendFoodId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Recommend Food ID"));
                    PlanFoodDTO foodDTO = new PlanFoodDTO(planDetailDTO.getStayTime(), recommendFood, new Plan(planDetailDTO.getPlanId()));
                    planService.savePlanFood(foodDTO);
                    break;
                case "event":
                    RecommendListEvent recommendEvent = recommendEventRepository.findById(planDetailDTO.getRecommendEventId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Recommend Event ID"));
                    PlanEventDTO eventDTO = new PlanEventDTO(planDetailDTO.getStayTime(), recommendEvent, new Plan(planDetailDTO.getPlanId()));
                    planService.savePlanEvent(eventDTO);
                    break;
                case "place":
                    RecommendListPlace recommendPlace = recommendPlaceRepository.findById(planDetailDTO.getRecommendPlaceId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Recommend Place ID"));
                    PlanPlaceDTO placeDTO = new PlanPlaceDTO(planDetailDTO.getStayTime(), recommendPlace, new Plan(planDetailDTO.getPlanId()));
                    planService.savePlanPlace(placeDTO);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid category");
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error processing plan detail: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}