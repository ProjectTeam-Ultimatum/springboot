package ultimatum.project.service.recommned;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.domain.entity.place.RecommendPlace;
import ultimatum.project.dto.event.RecommendListEventResponse;
import ultimatum.project.dto.food.RecommendFoodResponse;
import ultimatum.project.dto.food.RecommendListFoodResponse;
import ultimatum.project.dto.hotel.RecommendHotelResponse;
import ultimatum.project.dto.hotel.RecommendListHotelResponse;
import ultimatum.project.dto.image.RecommendImageFoodResponse;
import ultimatum.project.dto.image.RecommendImageHotelResponse;
import ultimatum.project.dto.image.RecommendImagePlaceResponse;
import ultimatum.project.dto.place.RecommendListPlaceResponse;
import ultimatum.project.dto.place.RecommendPlaceResponse;
import ultimatum.project.repository.RecommendImageRepository;
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendFoodRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.hotel.RecommendHotelRepository;
import ultimatum.project.repository.hotel.RecommendListHotelRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;
import ultimatum.project.repository.place.RecommendPlaceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor //공통
@Transactional
public class RecommendService {

    private final RecommendFoodRepository recommendListRepository;
    private final RecommendHotelRepository recommendHotelRepository;
    private final RecommendPlaceRepository recommendPlaceRepository;

    private RecommendListFoodRepository recommendListFoodRepository;
    private RecommendListPlaceRepository recommendListPlaceRepository;
    private RecommendListHotelRepository recommendListHotelRepository;
    private RecommendListEventRepository recommendListEventRepository;
    private ModelMapper modelMapper;
    private RecommendImageRepository recommendImageRepository;

    @Autowired
    public void RecommendImageService(RecommendImageRepository recommendImageRepository,
                                      RecommendListFoodRepository recommendListFoodRepository,
                                      RecommendListPlaceRepository recommendListPlaceRepository,
                                      RecommendListHotelRepository recommendListHotelRepository,
                                      RecommendListEventRepository recommendListEventRepository,
                                      ModelMapper modelMapper) {
        this.recommendImageRepository = recommendImageRepository;
        this.recommendListFoodRepository = recommendListFoodRepository;
        this.recommendListPlaceRepository = recommendListPlaceRepository;
        this.recommendListHotelRepository = recommendListHotelRepository;
        this.recommendListEventRepository = recommendListEventRepository;
        this.modelMapper = modelMapper;
    }


    //food 음식점
//    public Page<RecommendListFoodResponse> findRecommendListFood(Pageable pageable) {
//        return recommendListFoodRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListFoodResponse.class));
//    }

    //food 음식점 페이징 처리
//    public Page<RecommendListFoodResponse> findRecommendListFood(Pageable pageable) {
//        // 페이지 번호를 0부터 시작하도록 조정
//        pageable = PageRequest.of(
//                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
//                pageable.getPageSize(),
//                pageable.getSort()
//        );
//
//        // 조회된 페이지 데이터를 반환
//        return recommendListFoodRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListFoodResponse.class));
//    }

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
            // 관광지 리스트 데이터를 반환
            results = recommendListFoodRepository.findAll(pageable);
        }

        return results.map(entity -> modelMapper.map(entity, RecommendListFoodResponse.class));
    }

    //place 관광지
//    public Page<RecommendListPlaceResponse> findRecommendListPlace(Pageable pageable) {
//        return recommendListPlaceRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListPlaceResponse.class));
//    }

//    public Page<RecommendListPlaceResponse> findRecommendListPlace(Pageable pageable) {
//        // 페이지 번호를 0부터 시작하도록 조정
//        pageable = PageRequest.of(
//                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
//                pageable.getPageSize(),
//                pageable.getSort()
//        );
//
//        // 조회된 페이지 데이터를 반환
//        return recommendListPlaceRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListPlaceResponse.class));
//    }

//    public Page<RecommendListPlaceResponse> findRecommendListPlace(String tag, Pageable pageable) {
//        // 페이지 번호를 0부터 시작하도록 조정
//        pageable = PageRequest.of(
//                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
//                pageable.getPageSize(),
//                pageable.getSort()
//        );
//
//        Page<RecommendListPlace> results;
//        if (tag != null && !tag.isEmpty()) {
//            // recommendPlaceTag를 기준으로 필터링된 페이지 데이터를 반환
//            results = recommendListPlaceRepository.findByRecommendPlaceTagContainingIgnoreCase(tag, pageable);
//        } else {
//            // 모든 맛집 리스트 데이터를 반환
//            results = recommendListPlaceRepository.findAll(pageable);
//        }
//
//        return results.map(entity -> modelMapper.map(entity, RecommendListPlaceResponse.class));
//    }

    public Page<RecommendListPlaceResponse> findRecommendListPlace(String tag, String region, Pageable pageable) {
        // 페이지 번호를 0부터 시작하도록 조정
        pageable = PageRequest.of(
                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<RecommendListPlace> results;
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

        return results.map(entity -> modelMapper.map(entity, RecommendListPlaceResponse.class));
    }


    //hotel 숙박
//    public Page<RecommendListHotelResponse> findRecommendListHotel(Pageable pageable) {
//        return recommendListHotelRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListHotelResponse.class));
//    }

//    public Page<RecommendListHotelResponse> findRecommendListHotel(Pageable pageable) {
//        // 페이지 번호를 0부터 시작하도록 조정
//        pageable = PageRequest.of(
//                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
//                pageable.getPageSize(),
//                pageable.getSort()
//        );
//
//        // 조회된 페이지 데이터를 반환
//        return recommendListHotelRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListHotelResponse.class));
//    }

//    public Page<RecommendListHotelResponse> findRecommendListHotel(String tag, Pageable pageable) {
//        // 페이지 번호를 0부터 시작하도록 조정
//        pageable = PageRequest.of(
//                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
//                pageable.getPageSize(),
//                pageable.getSort()
//        );
//
//        Page<RecommendListHotel> results;
//        if (tag != null && !tag.isEmpty()) {
//            // recommendHotelTag를 기준으로 필터링된 페이지 데이터를 반환
//            results = recommendListHotelRepository.findByRecommendHotelTagContainingIgnoreCase(tag, pageable);
//        } else {
//            // 모든 맛집 리스트 데이터를 반환
//            results = recommendListHotelRepository.findAll(pageable);
//        }
//
//        return results.map(entity -> modelMapper.map(entity, RecommendListHotelResponse.class));
//    }

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

    //event 축제행사
//    public Page<RecommendListEventResponse> findRecommendListEvent(Pageable pageable) {
//        return recommendListEventRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListEventResponse.class));
//    }

//    public Page<RecommendListEventResponse> findRecommendListEvent(Pageable pageable) {
//        // 페이지 번호를 0부터 시작하도록 조정
//        pageable = PageRequest.of(
//                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
//                pageable.getPageSize(),
//                pageable.getSort()
//        );
//
//        // 조회된 페이지 데이터를 반환
//        return recommendListEventRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListEventResponse.class));
//    }

//    public Page<RecommendListEventResponse> findRecommendListEvent(String tag, Pageable pageable) {
//        // 페이지 번호를 0부터 시작하도록 조정
//        pageable = PageRequest.of(
//                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
//                pageable.getPageSize(),
//                pageable.getSort()
//        );
//
//        Page<RecommendListEvent> results;
//        if (tag != null && !tag.isEmpty()) {
//            // recommendEventTag를 기준으로 필터링된 페이지 데이터를 반환
//            results = recommendListEventRepository.findByRecommendEventTagContainingIgnoreCase(tag, pageable);
//        } else {
//            // 모든 맛집 리스트 데이터를 반환
//            results = recommendListEventRepository.findAll(pageable);
//        }
//
//        return results.map(entity -> modelMapper.map(entity, RecommendListEventResponse.class));
//    }

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




    // 메뉴 전체 조회
//    public List<RecommendListDTO> findRecommendList() {
//
//        List<RecommendFood> recommendList = recommendListRepository.findAll(Sort.by("recommendFoodId").descending());
//
//        // 로그 출력
//        log.info("전체 추천 음식 조회 결과: {}", recommendList);
//
//        //Review 엔티티들을 ReadReviewResponse DTO로 변환
//        return recommendList.stream()
//                .map(recommend -> modelMapper.map(recommend, RecommendListDTO.class))
//                .collect(Collectors.toList());
//
//    }


}
