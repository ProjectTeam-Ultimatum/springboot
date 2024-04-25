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
    private String recommendReplyTagValue;  // JSON 형태의 태그 목록을 저장

    public List<String> getRecommendReplyTagValue() {
        return JsonUtil.fromJson(this.recommendReplyTagValue);
    }

    public void setRecommendReplyTagValue(List<String> tags) {
        this.recommendReplyTagValue = JsonUtil.toJson(tags);
    }

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