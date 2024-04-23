package ultimatum.project.domain.entity.recommendReply;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter //엔티티에는 빼기
@AllArgsConstructor
@NoArgsConstructor
public class RecommendReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendReplyId;
    private String recommendReply;
    private Long recommendReplyStar;
    private String recommendReplyTagValue;  // JSON 형태의 태그 목록을 저장

    // JSON 형태의 태그 목록을 List<String>으로 반환하는 getter
    public List<String> getRecommendReplyTagValue() {
        if (this.recommendReplyTagValue == null || this.recommendReplyTagValue.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(this.recommendReplyTagValue, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            // JSON 변환 실패 시 빈 리스트 반환
            return new ArrayList<>();
        }
    }

    // List<String> 형태의 태그 목록을 JSON 문자열로 설정하는 setter
    public void setRecommendReplyTagValue(List<String> tags) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.recommendReplyTagValue = mapper.writeValueAsString(tags);
        } catch (JsonProcessingException e) {
            this.recommendReplyTagValue = "[]";
        }
    }

    @JsonIgnore
    public String getRecommendReplyTagsAsString() {
        // DB 저장용 raw 문자열 태그 목록을 가져오는 추가 getter
        return this.recommendReplyTagValue;
    }

    @ManyToOne
    @JoinColumn(name = "recommend_place_id")
    private RecommendListPlace recommendPlaceId;//객체임, id 붙이는거 적합하지 않음
    @ManyToOne
    @JoinColumn(name = "recommend_food_id")
    private RecommendListFood recommendFoodId;
    @ManyToOne
    @JoinColumn(name = "recommend_hotel_id")
    private RecommendListHotel recommendHotelId;
    @ManyToOne
    @JoinColumn(name = "recommend_event_id")
    private RecommendListEvent recommendEventId;
}