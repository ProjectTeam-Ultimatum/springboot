package ultimatum.project.domain.entity.Map;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;

    @PostMapping("/saveMap")
    public ResponseEntity<MapDTO> saveMap(@RequestBody MapDTO mapDTO){
        log.info("😎 review : {}", mapDTO.review);
        log.info("😎 title : {}", mapDTO.title);
        log.info("😎 grade : {}", mapDTO.grade);
        log.info("😎 lon : {}", mapDTO.lonCopy);
        log.info("😎 lat : {}", mapDTO.latCopy);
        mapDTO.setId(null);
        MapDTO saveMap = mapService.savemap(mapDTO);
        return ResponseEntity.ok(saveMap);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapDTO> readMap(@PathVariable Long id) {
        MapDTO readMap = mapService.readmap(id);

        return new ResponseEntity<>(readMap, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MapDTO> updateMap(@PathVariable Long id, @RequestBody MapDTO mapDTO) {
        MapDTO updateMap = mapService.updatemap(id, mapDTO);

        return new ResponseEntity<>(updateMap, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MapDTO> deleteMap(@PathVariable Long id) {
        MapDTO deleteMap = mapService.deletemap(id);

        return new ResponseEntity<>(deleteMap, HttpStatus.OK);
    }

    @GetMapping("/listMap")
    public ResponseEntity<?> listMap() {
        return ResponseEntity.ok().body(mapService.listmap());
    }
}
