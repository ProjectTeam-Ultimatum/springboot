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
import ultimatum.project.global.config.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendReplyId;
    private String recommendReply;
    private Long recommendReplyStar;
    /**
     * 태그 목록을 JSON 형태의 문자열로 저장
     * ["string1", "string2"]
     * */
    private String recommendReplyTagValue;

    // 저장된 JSON 문자열을 List<String>으로 변환하여 태그를 검색할 때 사용
    public List<String> getRecommendReplyTagValue() {
        return JsonUtil.fromJson(this.recommendReplyTagValue);
    }

    // 태그 목록을 JSON 문자열로 변환하여 저장
    public void setRecommendReplyTagValue(List<String> tags) {
        this.recommendReplyTagValue = JsonUtil.toJson(tags);
    }

    /**
     * 원시 JSON 데이터 직접 접근
     * 데이터베이스에 저장된 태그 정보가 JSON 문자열 형태로 되어 있을 때
     *  JSON 문자열을 그대로 가져옴
     *  변환 없이 원본 데이터 형태로 접근
     * */
    @JsonIgnore
    public String getRecommendReplyTagsAsString() {
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