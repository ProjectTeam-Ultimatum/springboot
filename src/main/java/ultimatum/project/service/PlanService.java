package ultimatum.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ultimatum.project.domain.dto.plan.PlanEventDTO;
import ultimatum.project.domain.dto.plan.PlanFoodDTO;
import ultimatum.project.domain.dto.plan.PlanPlaceDTO;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.plan.*;
import ultimatum.project.repository.PlanDayRepository;
import ultimatum.project.repository.PlanRepository;
import ultimatum.project.domain.dto.PlanRequest; // 필요한 경우 import 추가
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.repository.plan.PlanEventRepository;
import ultimatum.project.repository.plan.PlanFoodRepository;
import ultimatum.project.repository.plan.PlanPlaceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

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
    private RecommendListFoodRepository recommendFoodRepository;
    @Autowired
    private RecommendListEventRepository recommendEventRepository;
    @Autowired
    private RecommendListPlaceRepository recommendPlaceRepository;


    public Plan createPlan(PlanRequest planRequest) {
        System.out.println("Received plan days: " + planRequest.getPlanDays().size());

        if (planRequest.getPlanStartDay() == null || planRequest.getPlanEndDay() == null) {
            System.out.println("Start and end dates are required."); // 조건 불충족 로그
            throw new IllegalArgumentException("시작날짜와 종료날짜는 필수입니다.");
        }

        Plan plan = Plan.builder()
                .memberId(planRequest.getMemberId())
                .planStartDay(planRequest.getPlanStartDay())
                .planEndDay(planRequest.getPlanEndDay())
                .planTitle(planRequest.getPlanTitle())
                .build();

        plan = planRepository.save(plan);
        System.out.println("Plan saved with ID: " + plan.getPlanId()); // 저장된 Plan의 ID 로그

        List<PlanDay> planDays = createPlanDays(plan, planRequest.getPlanDays());
        planDayRepository.saveAll(planDays);
        System.out.println("Plan days saved: " + planDays.size()); // 저장된 PlanDay의 수 로그

        return plan;
    }

    private List<PlanDay> createPlanDays(Plan plan, List<PlanRequest.PlanDayRequest> planDaysRequest) {
        return planDaysRequest.stream().map(dayRequest -> {
            LocalDate date = dayRequest.getDate();
            LocalDateTime startTime = dayRequest.getStartTime();
            LocalDateTime endTime = dayRequest.getEndTime();

            PlanDay planDay = PlanDay.builder()
                    .planDate(date)
                    .planDayStartTime(startTime)
                    .planDayFinishTime(endTime)
                    .planId(plan) // 'planId' 필드에 'plan' 객체를 설정
                    .build();
            System.out.println("PlanDay created for date: " + date); // PlanDay 생성 로깅
            return planDay;
        }).collect(Collectors.toList());
    }
    public PlanFood savePlanFood(PlanFoodDTO planFoodDTO) {
        // DTO에서 직접 RecommendListFood 객체를 가져옵니다.
        PlanFood planFood = PlanFood.builder()
                .planId(planFoodDTO.getPlanId())
                .recommendListFoods(planFoodDTO.getRecommendFoodId())
                .planFoodStayTime(planFoodDTO.getStayTime())
                .build();
        return planFoodRepository.save(planFood);
    }


    public PlanEvent savePlanEvent(PlanEventDTO planEventDTO) {
        PlanEvent planEvent = PlanEvent.builder()
                .planId(planEventDTO.getPlanId())
                .recommendListEvents(planEventDTO.getRecommendEventId())
                .planEventStayTime(planEventDTO.getStayTime())
                .build();
        return planEventRepository.save(planEvent);
    }


    public PlanPlace savePlanPlace(PlanPlaceDTO planPlaceDTO) {
        PlanPlace planPlace = PlanPlace.builder()
                .planId(planPlaceDTO.getPlanId())
                .recommendListPlaces(planPlaceDTO.getRecommendPlaceId())
                .planPlaceStayTime(planPlaceDTO.getStayTime())
                .build();
        return planPlaceRepository.save(planPlace);
    }
}
