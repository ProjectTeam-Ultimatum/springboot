package ultimatum.project.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.PlanRequest;
import ultimatum.project.domain.dto.plan.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.plan.*;
import ultimatum.project.repository.PlanDayRepository;
import ultimatum.project.repository.PlanRepository;
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.hotel.RecommendListHotelRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.repository.plan.PlanEventRepository;
import ultimatum.project.repository.plan.PlanFoodRepository;
import ultimatum.project.repository.plan.PlanHotelRepository;
import ultimatum.project.repository.plan.PlanPlaceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        planRepository.save(plan);

        List<PlanDay> planDays = createAndSavePlanDays(plan, planRequest.getPlanDays());
        planDays.forEach(planDay -> savePlanDetails(plan, planDay, planRequest));

        return plan;
    }


    public void savePlanDetails(Plan plan, PlanDay planDay, PlanRequest planRequest) {
        savePlanFoods(plan, planDay, planRequest.getPlanFoods());
        savePlanEvents(plan, planDay, planRequest.getPlanEvents());
        savePlanPlaces(plan, planDay, planRequest.getPlanPlaces());
        savePlanHotels(planDay, planRequest.getPlanHotels());
    }

    private void savePlanFoods(Plan plan, PlanDay planDay, List<PlanFoodDTO> planFoods) {
        for (PlanFoodDTO dto : planFoods) {
            if (!planFoodRepository.existsByPlanAndRecommendFoodId(plan.getPlanId(), dto.getRecommendFoodId())) {
                RecommendListFood food = recommendFoodRepository.findById(dto.getRecommendFoodId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid food ID: " + dto.getRecommendFoodId()));
                PlanFood planFood = new PlanFood(plan, planDay, food, dto.getStayTime());
                planFoodRepository.save(planFood);
            }
        }
    }

    private void savePlanEvents(Plan plan, PlanDay planDay, List<PlanEventDTO> planEvents) {
        for (PlanEventDTO dto : planEvents) {
            if (!planEventRepository.existsByPlanIdAndEventId(plan.getPlanId(), dto.getRecommendEventId())) {
                RecommendListEvent event = recommendEventRepository.findById(dto.getRecommendEventId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + dto.getRecommendEventId()));
                PlanEvent planEvent = new PlanEvent(plan, planDay, event, dto.getStayTime());
                planEventRepository.save(planEvent);
            }
        }
    }

    private void savePlanPlaces(Plan plan, PlanDay planDay, List<PlanPlaceDTO> planPlaces) {
        for (PlanPlaceDTO dto : planPlaces) {
            if (!planPlaceRepository.existsByPlanIdAndPlaceId(plan.getPlanId(), dto.getRecommendPlaceId())) {
                RecommendListPlace place = recommendPlaceRepository.findById(dto.getRecommendPlaceId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid place ID: " + dto.getRecommendPlaceId()));
                PlanPlace planPlace = new PlanPlace(plan, planDay, place, dto.getStayTime());
                planPlaceRepository.save(planPlace);
            }
        }
    }

    private void savePlanHotels(PlanDay planDay, List<PlanHotelDTO> planHotels) {
        //
        planHotels.forEach(hotelDTO -> {
            RecommendListHotel hotel = recommendHotelRepository.findById(hotelDTO.getRecommendHotelId())
                    .orElseThrow(() -> new RuntimeException("Hotel not found with ID: " + hotelDTO.getRecommendHotelId()));

            log.info("🎉 roof");


            planHotelRepository.findByPlanDayId(planDay.getPlanDayId());

            if (!planHotelRepository.existsByPlanDayAndRecommendHotelId(planDay.getPlanDayId(), hotelDTO.getRecommendHotelId())) {
                log.info("🐱‍💻 plandayId : {}", planDay.getPlanDayId());
                PlanHotel planHotel = PlanHotel.builder()
                        .planDay(planDay)
                        .recommendHotel(hotel)
                        .build();
                planHotelRepository.save(planHotel);
            }
        });
    }

    public void savePlanFood(PlanFoodDTO planFoodDTO) {
        Plan plan = planRepository.findById(planFoodDTO.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID: " + planFoodDTO.getPlanId()));

        if (!planFoodRepository.existsByPlanAndRecommendFoodId(plan.getPlanId(), planFoodDTO.getRecommendFoodId())) {
            RecommendListFood food = recommendFoodRepository.findById(planFoodDTO.getRecommendFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid food ID: " + planFoodDTO.getRecommendFoodId()));
            PlanFood planFood = new PlanFood(plan, null, food, planFoodDTO.getStayTime());  // 여기에서 PlanDay를 null로 설정하는 것을 개선해야 함
            planFoodRepository.save(planFood);
        }
    }

    public void savePlanEvent(PlanEventDTO planEventDTO) {
        Plan plan = planRepository.findById(planEventDTO.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID: " + planEventDTO.getPlanId()));

        if (!planEventRepository.existsByPlanIdAndEventId(plan.getPlanId(), planEventDTO.getRecommendEventId())) {
            RecommendListEvent event = recommendEventRepository.findById(planEventDTO.getRecommendEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + planEventDTO.getRecommendEventId()));
            PlanEvent planEvent = new PlanEvent(plan, null, event, planEventDTO.getStayTime());  // 여기에서 PlanDay를 null로 설정하는 것을 개선해야 함
            planEventRepository.save(planEvent);
        }
    }

    public void savePlanPlace(PlanPlaceDTO planPlaceDTO) {
        Plan plan = planRepository.findById(planPlaceDTO.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID: " + planPlaceDTO.getPlanId()));

        if (!planPlaceRepository.existsByPlanIdAndPlaceId(plan.getPlanId(), planPlaceDTO.getRecommendPlaceId())) {
            RecommendListPlace place = recommendPlaceRepository.findById(planPlaceDTO.getRecommendPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + planPlaceDTO.getRecommendPlaceId()));
            PlanPlace planPlace = new PlanPlace(plan, null, place, planPlaceDTO.getStayTime());  // 여기에서 PlanDay를 null로 설정하는 것을 개선해야 함
            planPlaceRepository.save(planPlace);
        }
    }


    public PlanHotel savePlanHotel(PlanHotelDTO planHotelDTO) {
        PlanDay planDay = planDayRepository.findById(planHotelDTO.getPlanDayId())
                .orElseThrow(() -> new IllegalArgumentException("PlanDay ID " + planHotelDTO.getPlanDayId() + " not found"));

        if (!planHotelRepository.existsByPlanDayAndRecommendHotel_RecommendHotelId(planDay, planHotelDTO.getRecommendHotelId())) {
            RecommendListHotel hotel = recommendHotelRepository.findById(planHotelDTO.getRecommendHotelId())
                    .orElseThrow(() -> new RuntimeException("Hotel not found with ID: " + planHotelDTO.getRecommendHotelId()));
            PlanHotel planHotel = new PlanHotel(planDay, hotel);
            return planHotelRepository.save(planHotel);
        }
        // 만약 이미 저장된 PlanHotel 객체가 있을 경우에는 null이 아닌 해당 PlanHotel 객체를 반환하도록 수정할 수 있습니다.
        return null;
    }




    public List<PlanDay> createAndSavePlanDays(Plan plan, List<PlanRequest.PlanDayRequest> planDaysRequest) {
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
