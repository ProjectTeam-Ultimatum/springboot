package ultimatum.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.plan.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.plan.*;
import ultimatum.project.repository.PlanDayRepository;
import ultimatum.project.repository.PlanRepository;
import ultimatum.project.domain.dto.PlanRequest; // 필요한 경우 import 추가
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.hotel.RecommendListHotelRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.repository.plan.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    private final Logger logger = LoggerFactory.getLogger(PlanService.class);
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanDayRepository planDayRepository;
    @Autowired
    private PlanFoodRepository planFoodRepository;
    @Autowired
    private PlanEventRepository planEventRepository;
    @Autowired
    private PlanPlaceRepository planPlaceRepository;
    @Autowired
    private PlanHotelRepository planHotelRepository;
    @Autowired
    private RecommendListHotelRepository recommendHotelRepository;
    @Autowired
    private RecommendListFoodRepository recommendFoodRepository;
    @Autowired
    private RecommendListPlaceRepository recommendPlaceRepository;
    @Autowired
    private RecommendListEventRepository recommendEventRepository;


    public Plan createPlan(PlanRequest planRequest) {
        logger.info("Received plan request: {}", planRequest);

        if (planRequest.getPlanStartDay() == null || planRequest.getPlanEndDay() == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }

        Plan plan = Plan.builder()
                .memberId(planRequest.getMemberId())
                .planStartDay(planRequest.getPlanStartDay())
                .planEndDay(planRequest.getPlanEndDay())
                .planTitle(planRequest.getPlanTitle())
                .build();
        plan = planRepository.save(plan);

        List<PlanDay> planDays = createAndSavePlanDays(plan, planRequest.getPlanDays());
        final Plan finalPlan = plan;
        planDays.forEach(planDay -> {
            savePlanDetails(finalPlan.getPlanId(), planDay.getPlanDayId(), planRequest);
        });

        return plan;
    }

    private void savePlanDetails(Long planId, Long planDayId, PlanRequest planRequest) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID: " + planId));
        PlanDay planDay = planDayRepository.findById(planDayId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan day ID: " + planDayId));

        savePlanFoods(plan, planRequest.getPlanFoods());
        savePlanEvents(plan, planRequest.getPlanEvents());
        savePlanPlaces(plan, planRequest.getPlanPlaces());
        savePlanHotels(planDay, planRequest.getPlanHotels());  // 이제 planDay 객체 사용
    }

    private void savePlanFoods(Plan plan, List<PlanFoodDTO> planFoods) {
        for (PlanFoodDTO dto : planFoods) {
            RecommendListFood food = recommendFoodRepository.findById(dto.getRecommendFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid food ID: " + dto.getRecommendFoodId()));
            PlanFood planFood = new PlanFood();
            planFood.setPlan(plan); // Plan 객체 설정
            planFood.setRecommendListFoods(food);
            planFood.setPlanFoodStayTime(dto.getStayTime());
            planFoodRepository.save(planFood);
        }
    }




    private void savePlanPlaces(Plan plan, List<PlanPlaceDTO> planPlaces) {
        for (PlanPlaceDTO dto : planPlaces) {
            RecommendListPlace place = recommendPlaceRepository.findById(dto.getRecommendPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Place ID: " + dto.getRecommendPlaceId()));
            PlanPlace planPlace = new PlanPlace();
            planPlace.setPlan(plan); // Plan 객체 설정
            planPlace.setRecommendListPlaces(place);
            planPlace.setPlanPlaceStayTime(dto.getStayTime());
            planPlaceRepository.save(planPlace);
        }
    }

    private void savePlanEvents(Plan plan, List<PlanEventDTO> planEvents) {
        for (PlanEventDTO dto : planEvents) {
            RecommendListEvent event = recommendEventRepository.findById(dto.getRecommendEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Event ID: " + dto.getRecommendEventId()));
            PlanEvent planEvent = new PlanEvent();
            planEvent.setPlan(plan); // Plan 객체 설정
            planEvent.setRecommendListEvents(event);
            planEvent.setPlanEventStayTime(dto.getStayTime());
            planEventRepository.save(planEvent);
        }
    }

    private void savePlanHotels(PlanDay planDay, List<PlanHotelDTO> planHotels) {
        if (planHotels == null) {
            logger.warn("No plan hotels to save for PlanDay ID: {}", planDay.getPlanDayId());
            return;
        }

        planHotels.forEach(hotelDTO -> {
            RecommendListHotel hotel = recommendHotelRepository.findById(hotelDTO.getRecommendHotelId())
                    .orElseThrow(() -> new RuntimeException("Hotel not found with ID: " + hotelDTO.getRecommendHotelId()));

            PlanHotel planHotel = PlanHotel.builder()
                    .planDay(planDay)
                    .recommendHotel(hotel)
                    .build();

            planHotelRepository.save(planHotel);
        });
    }


    public PlanFood savePlanFood(PlanFoodDTO planFoodDTO) {
        // DTO에서 제공된 ID를 사용하여 RecommendListFood 객체를 찾습니다.
        RecommendListFood recommendFood = recommendFoodRepository.findById(planFoodDTO.getRecommendFoodId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid food ID: " + planFoodDTO.getRecommendFoodId()));

        PlanFood planFood = PlanFood.builder()
                .plan(planFoodDTO.getPlanId())
                .recommendListFoods(recommendFood)
                .planFoodStayTime(planFoodDTO.getStayTime())
                .build();
        return planFoodRepository.save(planFood);
    }

    public PlanEvent savePlanEvent(PlanEventDTO planEventDTO) {
        // DTO에서 제공된 ID를 사용하여 RecommendListEvent 객체를 찾습니다.
        RecommendListEvent recommendEvent = recommendEventRepository.findById(planEventDTO.getRecommendEventId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + planEventDTO.getRecommendEventId()));

        PlanEvent planEvent = PlanEvent.builder()
                .plan(planEventDTO.getPlanId())
                .recommendListEvents(recommendEvent)
                .planEventStayTime(planEventDTO.getStayTime())
                .build();
        return planEventRepository.save(planEvent);
    }

    public PlanPlace savePlanPlace(PlanPlaceDTO planPlaceDTO) {
        // DTO에서 제공된 ID를 사용하여 RecommendListPlace 객체를 찾습니다.
        RecommendListPlace recommendPlace = recommendPlaceRepository.findById(planPlaceDTO.getRecommendPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + planPlaceDTO.getRecommendPlaceId()));

        PlanPlace planPlace = PlanPlace.builder()
                .plan(planPlaceDTO.getPlanId())
                .recommendListPlaces(recommendPlace)
                .planPlaceStayTime(planPlaceDTO.getStayTime())
                .build();
        return planPlaceRepository.save(planPlace);
    }

    public PlanHotel savePlanHotel(Long planDayId, Long hotelId) {
        PlanDay planDay = planDayRepository.findById(planDayId)
                .orElseThrow(() -> new RuntimeException("PlanDay not found with the given ID"));
        RecommendListHotel hotel = recommendHotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with the given ID"));

        PlanHotel planHotel = PlanHotel.builder()
                .planDay(planDay) // 변경된 필드 이름 사용
                .recommendHotel(hotel) // 일관성을 위해 변경
                .build();
        return planHotelRepository.save(planHotel);
    }

    public  List<PlanDay> createAndSavePlanDays(Plan plan, List<PlanRequest.PlanDayRequest> planDaysRequest) {
        List<PlanDay> planDays = planDaysRequest.stream().map(dayRequest -> PlanDay.builder()
                .plan(plan)
                .planDate(dayRequest.getDate())
                .planDayStartTime(dayRequest.getStartTime())
                .planDayFinishTime(dayRequest.getFinishTime())
                .build()).collect(Collectors.toList());

        planDayRepository.saveAll(planDays);
        logger.info("Plan days saved: {}", planDays.size());
        return planDays;
    }
}
