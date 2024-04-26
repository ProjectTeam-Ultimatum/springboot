package ultimatum.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.HotelRequest;
import ultimatum.project.domain.dto.PlanRequest;
import ultimatum.project.domain.dto.plan.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.plan.*;
import ultimatum.project.repository.PlanRepository;
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.hotel.RecommendListHotelRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.service.PlanService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private RecommendListHotelRepository recommendHotelRepository;
    @Autowired
    private PlanRepository planRepository;

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
            logger.info("Plan created with ID: {}", plan.getPlanId());
            return ResponseEntity.ok().body(Map.of("planId", plan.getPlanId())); // 응답에 planId 포함
        } catch (Exception e) {
            logger.error("Failed to create plan", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating plan");
        }
    }



    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        logger.error("Failed to parse date/time: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Failed to parse date/time: " + ex.getParsedString());
    }

    // 장소 저장
    @PostMapping("/{category}/add")
    public ResponseEntity<?> addPlanDetail(@PathVariable String category, @RequestBody PlanDetailDTO planDetailDTO) {
        try {
            Plan plan = planRepository.findById(planDetailDTO.getPlanId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Plan ID"));

            switch (category) {
                case "food":
                    PlanFoodDTO foodDTO = new PlanFoodDTO(planDetailDTO.getStayTime(), planDetailDTO.getRecommendFoodId(), plan.getPlanId());
                    planService.savePlanFood(foodDTO);
                    break;
                case "event":
                    PlanEventDTO eventDTO = new PlanEventDTO(planDetailDTO.getStayTime(), planDetailDTO.getRecommendEventId(), plan.getPlanId());
                    planService.savePlanEvent(eventDTO);
                    break;
                case "place":
                    PlanPlaceDTO placeDTO = new PlanPlaceDTO(planDetailDTO.getStayTime(), planDetailDTO.getRecommendPlaceId(), plan.getPlanId());
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

    @PostMapping("/addHotel")
    public ResponseEntity<?> addHotel(@RequestBody HotelRequest hotelRequest) {
        try {
            PlanHotelDTO planHotelDTO = new PlanHotelDTO();
            planHotelDTO.setPlanDayId(hotelRequest.getPlanDayId());
            planHotelDTO.setRecommendHotelId(hotelRequest.getHotelId());

            PlanHotel planHotel = planService.savePlanHotel(planHotelDTO);
            return ResponseEntity.ok(planHotel);
        } catch (RuntimeException e) {
            logger.error("Error adding hotel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
