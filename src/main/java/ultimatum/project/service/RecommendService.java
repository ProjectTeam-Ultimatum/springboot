package ultimatum.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.event.RecommendListEventResponse;
import ultimatum.project.domain.dto.food.RecommendListFoodResponse;
import ultimatum.project.domain.dto.hotel.RecommendListHotelResponse;
import ultimatum.project.domain.dto.place.RecommendListPlaceResponse;
import ultimatum.project.repository.plan.PlanPlaceRepository;
import ultimatum.project.repository.event.RecommendListEventRepository;
import ultimatum.project.repository.food.RecommendListFoodRepository;
import ultimatum.project.repository.hotel.RecommendListHotelRepository;
import ultimatum.project.repository.place.RecommendListPlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {
    private final RecommendListFoodRepository foodRepository;
    private final RecommendListPlaceRepository placeRepository;
    private final RecommendListHotelRepository hotelRepository;
    private final RecommendListEventRepository eventRepository;
    private final ModelMapper modelMapper;
    

    public List<RecommendListFoodResponse> searchFoodsByTitle(String title, Pageable pageable) {
        return foodRepository.findByRecommendFoodTitleContainingIgnoreCase(title, pageable)
                .stream()
                .map(food -> modelMapper.map(food, RecommendListFoodResponse.class))
                .collect(Collectors.toList());
    }

    public List<RecommendListPlaceResponse> searchPlacesByTitle(String title, Pageable pageable) {
        return placeRepository.findByRecommendPlaceTitleContainingIgnoreCase(title, pageable)
                .stream()
                .map(place -> modelMapper.map(place, RecommendListPlaceResponse.class))
                .collect(Collectors.toList());
    }

    public List<RecommendListEventResponse> searchEventsByTitle(String title, Pageable pageable) {
        return eventRepository.findByRecommendEventTitleContainingIgnoreCase(title, pageable)
                .stream()
                .map(event -> modelMapper.map(event, RecommendListEventResponse.class))
                .collect(Collectors.toList());
    }

    public List<RecommendListHotelResponse> searchHotelsByTitle(String title, Pageable pageable) {
        return hotelRepository.findByRecommendHotelTitleContainingIgnoreCase(title, pageable)
                .stream()
                .map(hotel -> modelMapper.map(hotel, RecommendListHotelResponse.class))
                .collect(Collectors.toList());
    }

    public Page<RecommendListPlaceResponse> findRecommendListPlace(Pageable pageable) {
        return placeRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, RecommendListPlaceResponse.class));
    }

    public Page<RecommendListFoodResponse> findRecommendListFood(Pageable pageable) {
        return foodRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, RecommendListFoodResponse.class));
    }

    public Page<RecommendListHotelResponse> findRecommendListHotel(Pageable pageable) {
        return hotelRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, RecommendListHotelResponse.class));
    }

    public Page<RecommendListEventResponse> findRecommendListEvent(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, RecommendListEventResponse.class));
    }

    public Page<Object> findAllRecommendations(Pageable pageable) {
        List<Object> combinedResults = new ArrayList<>();
        var foods = foodRepository.findAll(pageable).map(entity -> modelMapper.map(entity, RecommendListFoodResponse.class)).getContent();
        var places = placeRepository.findAll(pageable).map(entity -> modelMapper.map(entity, RecommendListPlaceResponse.class)).getContent();
        var events = eventRepository.findAll(pageable).map(entity -> modelMapper.map(entity, RecommendListEventResponse.class)).getContent();

        combinedResults.addAll(foods);
        combinedResults.addAll(places);
        combinedResults.addAll(events);

        return new PageImpl<>(combinedResults, pageable, combinedResults.size());
    }

    public List<Object> searchAllCategoriesByTitle(String title, Pageable pageable) {
        List<RecommendListFoodResponse> foods = searchFoodsByTitle(title, pageable);
        List<RecommendListPlaceResponse> places = searchPlacesByTitle(title, pageable);
        List<RecommendListEventResponse> events = searchEventsByTitle(title, pageable);

        List<Object> allResults = new ArrayList<>();
        allResults.addAll(foods);
        allResults.addAll(places);
        allResults.addAll(events);
        return allResults;
    }
}
