package ultimatum.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.dto.food.RecommendListDTO;
import ultimatum.project.repository.RecommendImageRepository;
import ultimatum.project.repository.RecommendListRepository;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor //공통
@Transactional
public class RecommendListService {

    private final RecommendListRepository recommendListRepository;
    private final RecommendImageRepository recommendImageRepository;
    private final ModelMapper modelMapper;

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

    //메뉴 전체 조회
    @Transactional(readOnly = true)
    public Page<RecommendListDTO> readAllList(Pageable pageable) {

        try {
            //페이지네이션을 적용하여 recommendList 엔티티 조회
            Page<RecommendFood> recommendList = recommendListRepository.findAll(pageable);

            //단일 이미지를 리스트에 넣음
            return recommendList.map(list -> new RecommendListDTO(
                    list.getRecommendFoodId(),
                    list.getRecommendFoodTitle(),
                    list.getRecommendFoodSubtitle(),
                    list.getRecommendFoodAddress(),
                    list.getRecommendFoodContent(),
                    list.getRecommendFoodOpentime(),
                    list.getRecommendFoodClosetime(),
                    list.getRecommendFoodStar(),
                    list.getRecommendFoodLatitude(),
                    list.getRecommendFoodLongitude(),
                    list.getRecommendFoodBudget(),
                    list.getRecommendFoodCategory()
            ));
            //예외 처리
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

}
