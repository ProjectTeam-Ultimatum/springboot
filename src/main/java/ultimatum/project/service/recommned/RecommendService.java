package ultimatum.project.service.recommned;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.event.RecommendListEventByIdResponse;
import ultimatum.project.domain.dto.food.RecommendListFoodByIdResponse;
import ultimatum.project.domain.dto.hotel.RecommendListHotelByIdResponse;
import ultimatum.project.domain.dto.place.RecommendListPlaceByIdResponse;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.dto.event.RecommendListEventResponse;
import ultimatum.project.domain.dto.food.RecommendListFoodResponse;
import ultimatum.project.domain.dto.hotel.RecommendListHotelResponse;
import ultimatum.project.domain.dto.place.RecommendListPlaceResponse;
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendFoodRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.hotel.RecommendHotelRepository;
import ultimatum.project.repository.hotel.RecommendListHotelRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.repository.place.RecommendPlaceRepository;

@Log4j2
@Service
@RequiredArgsConstructor //공통
@Transactional
public class RecommendService {

    //final 필드는 생성자 주입을 통해 초기화, 불변성
    //@RequiredArgsConstructor 어노테이션에 의해 생성자를 통해 주입받습니다.
    //Repository를 주입받아 데이터를 관리
    private final RecommendFoodRepository recommendListRepository;
    private final RecommendHotelRepository recommendHotelRepository;
    private final RecommendPlaceRepository recommendPlaceRepository;

    //@Autowired 어노테이션을 사용하여 setter 메서드에서 주입
    private RecommendListFoodRepository recommendListFoodRepository;
    private RecommendListPlaceRepository recommendListPlaceRepository;
    private RecommendListHotelRepository recommendListHotelRepository;
    private RecommendListEventRepository recommendListEventRepository;
    private ModelMapper modelMapper;

    //각 리포지토리와 ModelMapper 객체를 주입받아 클래스의 필드에 할당
    //클래스는 데이터베이스 작업과 객체 매핑을 수행할 수 있는 필요한 의존성을 갖춤
    @Autowired
    public void RecommendImageService(RecommendListFoodRepository recommendListFoodRepository,
                                      RecommendListPlaceRepository recommendListPlaceRepository,
                                      RecommendListHotelRepository recommendListHotelRepository,
                                      RecommendListEventRepository recommendListEventRepository,
                                      ModelMapper modelMapper) {
        this.recommendListFoodRepository = recommendListFoodRepository;
        this.recommendListPlaceRepository = recommendListPlaceRepository;
        this.recommendListHotelRepository = recommendListHotelRepository;
        this.recommendListEventRepository = recommendListEventRepository;
        this.modelMapper = modelMapper;
    }



    //음식점 전체 조회, 페이징 처리
    @Transactional(readOnly = true)
    public Page<RecommendListFoodResponse> findRecommendListFood(String tag, String region, Pageable pageable) {
        // 페이지 번호를 0부터 시작하도록 조정
        pageable = PageRequest.of(
                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<RecommendListFood> results;
        if (tag != null && !tag.isEmpty()) {
            // recommendFoodTag를 기준으로 필터링된 페이지 데이터를 반환
            results = recommendListFoodRepository.findByRecommendFoodTagContainingIgnoreCase(tag, pageable);
        } else if (region != null && !region.isEmpty()) {
            // recommendFoodRegion를 기준으로 필터링된 페이지 데이터를 반환
            results = recommendListFoodRepository.findByRecommendFoodRegionContainingIgnoreCase(region, pageable);
        } else {
            // 음식점 리스트 데이터를 반환
            results = recommendListFoodRepository.findAll(pageable);
        }

        return results.map(entity -> modelMapper.map(entity, RecommendListFoodResponse.class));
    }

    //음식점 Id 1건 조회
    @Transactional(readOnly = true)
    public RecommendListFoodByIdResponse getRecommendListFoodById(Long recommendFoodId) {
        RecommendListFood food = recommendListFoodRepository.findById(recommendFoodId)
                .orElseThrow(() -> new EntityNotFoundException("음식 추천 ID를 찾을 수 없습니다: " + recommendFoodId));

        return new RecommendListFoodByIdResponse(
                food.getRecommendFoodId(),
                food.getRecommendFoodTitle(),
                food.getRecommendFoodIntroduction(),
                food.getRecommendFoodAllTag(),
                food.getRecommendFoodTag(),
                food.getRecommendFoodCategory(),
                food.getRecommendFoodAddress(),
                food.getRecommendFoodRegion(),
                food.getRecommendFoodOpentime(),
                food.getRecommendFoodClosetime(),
                food.getRecommendFoodStar(), // 순서를 맞춰서
                food.getRecommendFoodLike(), // 이 라인과 교체
                food.getRecommendFoodLatitude(),
                food.getRecommendFoodLongitude(),
                food.getRecommendFoodPhoneNo(),
                food.getRecommendFoodImgPath(),
                food.getRecommendFoodBudget() // 마지막 콤마 제거
        );
    }


    //관광지 전체 조회, 페이징 처리
    //태그와 지역을 기반으로 관광지를 검색하고 페이징 처리된 결과를 반환
    public Page<RecommendListPlaceResponse> findRecommendListPlace(String tag, String region, Pageable pageable) {
        // 페이지 번호를 0부터 시작하도록 조정
        pageable = PageRequest.of(
                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<RecommendListPlace> results;
        //tag 또는 region에 따라 각각의 필터링된 결과를 RecommendListPlaceRepository를 통해 가져옴
        if (tag != null && !tag.isEmpty()) {
            // recommendPlaceTag를 기준으로 필터링된 페이지 데이터를 반환
            results = recommendListPlaceRepository.findByRecommendPlaceTagContainingIgnoreCase(tag, pageable);
        } else if (region != null && !region.isEmpty()) {
            // recommendPlaceRegion를 기준으로 필터링된 페이지 데이터를 반환
            results = recommendListPlaceRepository.findByRecommendPlaceRegionContainingIgnoreCase(region, pageable);
        } else {
            // 관광지 리스트 데이터를 반환
            results = recommendListPlaceRepository.findAll(pageable);
        }
        //결과는 RecommendListPlaceResponse DTO로 매핑
        return results.map(entity -> modelMapper.map(entity, RecommendListPlaceResponse.class));
    }

    //관광지 Id 1건 조회
    @Transactional(readOnly = true)
    public RecommendListPlaceByIdResponse getRecommendListPlaceById(Long recommendPlaceId) {
        //RecommendPlaceRepository를 통해 데이터를 가져오고, 없으면 EntityNotFoundException을 발생시킵니다.
        RecommendListPlace place = recommendListPlaceRepository.findById(recommendPlaceId)
                .orElseThrow(() -> new EntityNotFoundException("음식 추천 ID를 찾을 수 없습니다: " + recommendPlaceId));

        //결과는 RecommendListPlaceByIdResponse DTO로 매핑
        return new RecommendListPlaceByIdResponse(
                place.getRecommendPlaceId(),
                place.getRecommendPlaceTitle(),
                place.getRecommendPlaceIntroduction(),
                place.getRecommendPlaceAllTag(),
                place.getRecommendPlaceTag(),
                place.getRecommendPlaceCategory(),
                place.getRecommendPlaceAddress(),
                place.getRecommendPlaceRegion(),
                place.getRecommendPlaceOpentime(),
                place.getRecommendPlaceClosetime(),
                place.getRecommendPlaceStar(),
                place.getRecommendPlaceLike(),
                place.getRecommendPlaceLatitude(),
                place.getRecommendPlaceLongitude(),
                place.getRecommendPlacePhoneNo(),
                place.getRecommendPlaceImgPath(),
                place.getRecommendPlaceBudget()
        );
    }

    //숙박 전체 조회, 페이징 처리
    public Page<RecommendListHotelResponse> findRecommendListHotel(String tag, String region, Pageable pageable) {
        // 페이지 번호를 0부터 시작하도록 조정
        pageable = PageRequest.of(
                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<RecommendListHotel> results;
        if (tag != null && !tag.isEmpty()) {
            // recommendHotelTag를 기준으로 필터링된 페이지 데이터를 반환
            results = recommendListHotelRepository.findByRecommendHotelTagContainingIgnoreCase(tag, pageable);
        } else if (region != null && !region.isEmpty()) {
            // recommendHotelRegion를 기준으로 필터링된 페이지 데이터를 반환
            results = recommendListHotelRepository.findByRecommendHotelRegionContainingIgnoreCase(region, pageable);
        } else {
            // 관광지 리스트 데이터를 반환
            results = recommendListHotelRepository.findAll(pageable);
        }

        return results.map(entity -> modelMapper.map(entity, RecommendListHotelResponse.class));
    }

    //숙박 Id 1건 조회
    @Transactional(readOnly = true)
    public RecommendListHotelByIdResponse getRecommendListHotelById(Long recommendHotelId) {
        RecommendListHotel hotel = recommendListHotelRepository.findById(recommendHotelId)
                .orElseThrow(() -> new EntityNotFoundException("음식 추천 ID를 찾을 수 없습니다: " + recommendHotelId));

        return new RecommendListHotelByIdResponse(
                hotel.getRecommendHotelId(),
                hotel.getRecommendHotelTitle(),
                hotel.getRecommendHotelIntroduction(),
                hotel.getRecommendHotelAllTag(),
                hotel.getRecommendHotelTag(),
                hotel.getRecommendHotelCategory(),
                hotel.getRecommendHotelAddress(),
                hotel.getRecommendHotelRegion(),
                hotel.getRecommendHotelOpentime(),
                hotel.getRecommendHotelClosetime(),
                hotel.getRecommendHotelStar(), // 순서를 맞춰서
                hotel.getRecommendHotelLike(), // 이 라인과 교체
                hotel.getRecommendHotelLatitude(),
                hotel.getRecommendHotelLongitude(),
                hotel.getRecommendHotelPhoneNo(),
                hotel.getRecommendHotelImgPath(),
                hotel.getRecommendHotelBudget() // 마지막 콤마 제거
        );
    }


    //축제행사 전체조회, 페이징 처리
    public Page<RecommendListEventResponse> findRecommendListEvent(String tag, String region, Pageable pageable) {
        // 페이지 번호를 0부터 시작하도록 조정
        pageable = PageRequest.of(
                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<RecommendListEvent> results;
        if (tag != null && !tag.isEmpty()) {
            // recommendEventTag를 기준으로 필터링된 페이지 데이터를 반환
            results = recommendListEventRepository.findByRecommendEventTagContainingIgnoreCase(tag, pageable);
        } else if (region != null && !region.isEmpty()) {
            // recommendEventRegion를 기준으로 필터링된 페이지 데이터를 반환
            results = recommendListEventRepository.findByRecommendEventRegionContainingIgnoreCase(region, pageable);
        } else {
            // 관광지 리스트 데이터를 반환
            results = recommendListEventRepository.findAll(pageable);
        }

        return results.map(entity -> modelMapper.map(entity, RecommendListEventResponse.class));
    }


    //축제행사 Id 1건 조회
    @Transactional(readOnly = true)
    public RecommendListEventByIdResponse getRecommendListEventById(Long recommendEventId) {
        RecommendListEvent event = recommendListEventRepository.findById(recommendEventId)
                .orElseThrow(() -> new EntityNotFoundException("음식 추천 ID를 찾을 수 없습니다: " + recommendEventId));

        return new RecommendListEventByIdResponse(
                event.getRecommendEventId(),
                event.getRecommendEventTitle(),
                event.getRecommendEventIntroduction(),
                event.getRecommendEventAllTag(),
                event.getRecommendEventTag(),
                event.getRecommendEventCategory(),
                event.getRecommendEventAddress(),
                event.getRecommendEventRegion(),
                event.getRecommendEventOpentime(),
                event.getRecommendEventClosetime(),
                event.getRecommendEventStar(), // 순서를 맞춰서
                event.getRecommendEventLike(), // 이 라인과 교체
                event.getRecommendEventLatitude(),
                event.getRecommendEventLongitude(),
                event.getRecommendEventPhoneNo(),
                event.getRecommendEventImgPath(),
                event.getRecommendEventBudget() // 마지막 콤마 제거
        );
    }

}
