package ultimatum.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ultimatum.project.domain.entity.food.RecommendFood;
import ultimatum.project.dto.food.RecommendListDTO;
import ultimatum.project.repository.RecommendListRepository;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class RecommendListService {

    private final RecommendListRepository recommendListRepository;

    private final ModelMapper modelMapper;


    // 메뉴 전체 조회
    public List<RecommendListDTO> findRecommendList(){
        List<RecommendFood> recommendList = recommendListRepository.findAll(Sort.by("recommendFoodId").descending());

        // 로그 출력
        log.info("전체 추천 음식 조회 결과: {}", recommendList);

        return recommendList.stream()
                .map(recommend -> modelMapper.map(recommend, RecommendListDTO.class))
                .collect(Collectors.toList());


    }

    
    
}
