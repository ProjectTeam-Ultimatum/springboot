package ultimatum.project.domain.entity.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String addressCopy;
    private Integer grade;
    private String review;
    private Double lonCopy;
    private Double latCopy;
    private String image;
    private String mapTag;
    private String date;
    private Integer budget;
    private String course;
    private String category;

    public List<String> getMapTag() {
        if (this.mapTag == null || this.mapTag.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(this.mapTag, new TypeReference<List<String>>() {});
        }catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    public void MapTag(List<String> tags) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.mapTag = mapper.writeValueAsString(tags);
        } catch (JsonProcessingException e) {
            this.mapTag = "[]";
        }
    }

    @JsonIgnore
    public String getTag() {
        return this.mapTag;
    }
}



