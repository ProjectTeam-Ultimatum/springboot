package ultimatum.project.domain.entity.Map;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MapService {
    private final MapRepository mapRepository;

    @Transactional
    public MapDTO savemap(MapDTO mapDTO) {

        MapEntity mapEntity = MapEntity.builder()
                .title(mapDTO.getTitle())
                .grade(mapDTO.getGrade())
                .addressCopy(mapDTO.getAddressCopy())
                .review(mapDTO.getReview())
                .lonCopy(mapDTO.getLonCopy())
                .latCopy(mapDTO.getLatCopy())
                .image(mapDTO.getImage())
                .date(mapDTO.getDate())
                .budget(mapDTO.getBudget())
                .course(mapDTO.getCourse())
                .category(mapDTO.getCategory())
                .build();

        mapRepository.save(mapEntity);
        return mapDTO;
    }

    @Transactional
    public MapDTO readmap(Long id) {
        MapEntity foundmap = mapRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 Id로 조회된 게시글이 없습니다."));
        return MapDTO.builder()
                .title(foundmap.getTitle())
                .grade(foundmap.getGrade())
                .addressCopy(foundmap.getAddressCopy())
                .review(foundmap.getReview())
                .lonCopy(foundmap.getLonCopy())
                .latCopy(foundmap.getLatCopy())
                .image(foundmap.getImage())
                .mapTag(foundmap.getMapTag())
                .date(foundmap.getDate())
                .budget(foundmap.getBudget())
                .course(foundmap.getCourse())
                .category(foundmap.getCategory())
                .build();
    }

//    @Transactional
//    public MapDTO updatemap(Long id, MapDTO mapDTO) {
//        MapEntity foundmap = mapRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("해당 Id로 조회된 게시글이 없습니다."));
//
//        foundmap.update(mapDTO.getId(), mapDTO.getTitle(), mapDTO.getAddressCopy(), mapDTO.getGrade(), mapDTO.getReview(), mapDTO.getLonCopy(), mapDTO.getLatCopy());
//        foundmap = mapRepository.save(foundmap);
//        return MapDTO.builder()
//                .id(foundmap.getId())
//                .title(foundmap.getTitle())
//                .addressCopy(foundmap.getAddressCopy())
//                .grade(foundmap.getGrade())
//                .review(foundmap.getReview())
//                .lonCopy(foundmap.getLonCopy())
//                .latCopy(foundmap.getLatCopy())
//                .build();
//    }
//
//    @Transactional
//    public MapDTO deletemap(Long id) {
//        MapEntity foundmap = mapRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("해당 Id로 조회된 게시글이 없습니다."));
//
//        mapRepository.delete(foundmap);
//        return new MapDTO();
//
//
//    }

    @Transactional
    public List<MapDTO> listmap() {
        return mapRepository.findAll().stream().map(mapEntity -> new MapDTO(mapEntity.getId(), mapEntity.getTitle(), mapEntity.getAddressCopy(), mapEntity.getGrade(), mapEntity.getReview(), mapEntity.getLonCopy(), mapEntity.getLatCopy(), mapEntity.getImage(), mapEntity.getMapTag(), mapEntity.getDate(), mapEntity.getBudget(), mapEntity.getCourse(), mapEntity.getCategory())).collect(Collectors.toList());
    }

}
