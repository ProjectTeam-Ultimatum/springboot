package ultimatum.project.service;

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
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendHotel;
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


    // food 전체 조회
//    @Transactional(readOnly = true)
//    public Page<RecommendFoodResponse> readFoodAll(Pageable pageable) {
//
//        try {
//            //페이지네이션을 적용하여 recommendList 엔티티 조회
//            Page<RecommendFood> recommendListFood = recommendListRepository.findAll(pageable);
//
//            //단일 이미지를 리스트에 넣음
//            return recommendListFood.map(list -> {
//                // 이미지 URL을 가져오는 로직 필요
//               List<RecommendImageFoodResponse> foodImages = list.getRecommendImages().stream()
//                       .map(foodimage -> new RecommendImageFoodResponse(
//                               foodimage.getRecommendImageUrl()
//                       )).collect(Collectors.toList());
//
//                // DTO 객체 생성
//                return new RecommendFoodResponse(
//                        list.getRecommendFoodId(),
//                        list.getRecommendFoodTitle(),
//                        list.getRecommendFoodSubtitle(),
//                        list.getRecommendFoodAddress(),
//                        list.getRecommendFoodContent(),
//                        list.getRecommendFoodOpentime(),
//                        list.getRecommendFoodClosetime(),
//                        list.getRecommendFoodStar(),
//                        list.getRecommendFoodLatitude(),
//                        list.getRecommendFoodLongitude(),
//                        list.getRecommendFoodBudget(),
//                        list.getRecommendFoodCategory(),
//                        foodImages
//                );
//            });
//            //예외 처리
//        } catch (DataAccessException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 접근 중 오류가 발생했습니다.", e);
//        }
//    }

    //hotel 전체조회
//    @Transactional(readOnly = true)
//    public Page<RecommendHotelResponse> readHotelAll(Pageable pageable) {
//
//        try {
//            //페이지네이션을 적용하여 recommendList 엔티티 조회
//            Page<RecommendHotel> recommendListHotel = recommendHotelRepository.findAll(pageable);
//
//            //단일 이미지를 리스트에 넣음
//            return recommendListHotel.map(list -> {
//                // 이미지 URL을 가져오는 로직 필요
//                List<RecommendImageHotelResponse> hotelImages = list.getRecommendImages().stream()
//                        .map(hotelImage -> new RecommendImageHotelResponse(
//                                hotelImage.getRecommendImageUrl()
//                        )).collect(Collectors.toList());
//
//                // DTO 객체 생성
//                return new RecommendHotelResponse(
//                        list.getRecommendHotelId(),
//                        list.getRecommendHotelTitle(),
//                        list.getRecommendHotelSubtitle(),
//                        list.getRecommendHotelAddress(),
//                        list.getRecommendHotelContent(),
//                        list.getRecommendHotellAtitude(),
//                        list.getRecommendHotelLongitude(),
//                        list.getRecommendHotelLike(),
//                        list.getRecommendHotelStar(),
//                        list.getRecommendHotelBudget(),
//                        list.getRecommendHotelCategory(),
//                        hotelImages
//                );
//            });
//            //예외 처리
//        } catch (DataAccessException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 접근 중 오류가 발생했습니다.", e);
//        }
//    }

    //place 전체조회
//    @Transactional(readOnly = true)
//    public Page<RecommendPlaceResponse> readPlaceAll(Pageable pageable) {
//
//        try {
//            //페이지네이션을 적용하여 recommendList 엔티티 조회
//            Page<RecommendPlace> recommendListPlace = recommendPlaceRepository.findAll(pageable);
//
//            //단일 이미지를 리스트에 넣음
//            return recommendListPlace.map(list -> {
//                // 이미지 URL을 가져오는 로직 필요
//                List<RecommendImagePlaceResponse> placeImages = list.getRecommendImages().stream()
//                        .map(placeImage -> new RecommendImagePlaceResponse(
//                                placeImage.getRecommendImageUrl()
//                        )).collect(Collectors.toList());
//
//                // DTO 객체 생성
//                return new RecommendPlaceResponse(
//                        list.getRecommendPlaceId(),
//                        list.getRecommendPlaceTitle(),
//                        list.getRecommendPlaceSubtitle(),
//                        list.getRecommendPlaceAddress(),
//                        list.getRecommendPlaceContent(),
//                        list.getRecommendPlaceLatitude(),
//                        list.getRecommendPlaceLongitude(),
//                        list.getRecommendPlaceLike(),
//                        list.getRecommendPlaceStar(),
//                        list.getRecommendPlaceBudget(),
//                        list.getRecommendPlaceCategory(),
//                        placeImages
//                );
//            });
//            //예외 처리
//        } catch (DataAccessException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 접근 중 오류가 발생했습니다.", e);
//        }
//    }

    //food 음식점
//    public Page<RecommendListFoodResponse> findRecommendListFood(Pageable pageable) {
//        return recommendListFoodRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListFoodResponse.class));
//    }

    //food 음식점 페이징 처리
    public Page<RecommendListFoodResponse> findRecommendListFood(Pageable pageable) {
        // 페이지 번호를 0부터 시작하도록 조정
        pageable = PageRequest.of(
                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
                pageable.getPageSize(),
                pageable.getSort()
        );

        // 조회된 페이지 데이터를 반환
        return recommendListFoodRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, RecommendListFoodResponse.class));
    }

    //place 관광지
    public Page<RecommendListPlaceResponse> findRecommendListPlace(Pageable pageable) {
        return recommendListPlaceRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, RecommendListPlaceResponse.class));
    }

    //hotel 숙박
    public Page<RecommendListHotelResponse> findRecommendListHotel(Pageable pageable) {
        return recommendListHotelRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, RecommendListHotelResponse.class));
    }

    //event 축제행사
//    public Page<RecommendListEventResponse> findRecommendListEvent(Pageable pageable) {
//        return recommendListEventRepository.findAll(pageable)
//                .map(entity -> modelMapper.map(entity, RecommendListEventResponse.class));
//    }

    public Page<RecommendListEventResponse> findRecommendListEvent(Pageable pageable) {
        // 페이지 번호를 0부터 시작하도록 조정
        pageable = PageRequest.of(
                Math.max(pageable.getPageNumber() - 1, 0), // 0보다 작은 경우는 0으로 설정
                pageable.getPageSize(),
                pageable.getSort()
        );

        // 조회된 페이지 데이터를 반환
        return recommendListEventRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, RecommendListEventResponse.class));
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
