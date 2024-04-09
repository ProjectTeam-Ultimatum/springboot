package ultimatum.project.domain.entity.Map;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
                .build();

        mapRepository.save(mapEntity);
        return mapDTO;
    }

    @Transactional
    public MapDTO readmap(Long id) {
        MapEntity foundmap = mapRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 Id로 조회된 게시글이 없습니다."));
        return MapDTO.builder()
                .id(foundmap.getId())
                .title(foundmap.getTitle())
                .addressCopy(foundmap.getAddressCopy())
                .grade(foundmap.getGrade())
                .review(foundmap.getReview())
                .build();
    }

    @Transactional
    public MapDTO updatemap(Long id, MapDTO mapDTO) {
        MapEntity foundmap = mapRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 Id로 조회된 게시글이 없습니다."));

        foundmap.update(mapDTO.getId(), mapDTO.getTitle(), mapDTO.getAddressCopy(), mapDTO.getGrade(), mapDTO.getReview());
        foundmap = mapRepository.save(foundmap);
        return MapDTO.builder()
                .id(foundmap.getId())
                .title(foundmap.getTitle())
                .addressCopy(foundmap.getAddressCopy())
                .grade(foundmap.getGrade())
                .review(foundmap.getReview())
                .build();
    }

    @Transactional
    public MapDTO deletemap(Long id) {
        MapEntity foundmap = mapRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 Id로 조회된 게시글이 없습니다."));

        mapRepository.delete(foundmap);
        return new MapDTO();


    }


}
