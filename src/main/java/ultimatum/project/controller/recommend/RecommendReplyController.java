package ultimatum.project.controller.recommend;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.recommendReply.ReadRecommendReplyAllResponse;
import ultimatum.project.domain.dto.recommendReply.RecommendReplyResponse;
import ultimatum.project.domain.entity.event.RecommendListEvent;
import ultimatum.project.domain.entity.food.RecommendListFood;
import ultimatum.project.domain.entity.hotel.RecommendListHotel;
import ultimatum.project.domain.entity.place.RecommendListPlace;
import ultimatum.project.service.recommned.RecommendReplyService;

import java.util.List;

@Tag(name = "recommend", description = "평점정보")
@Log4j2
@CrossOrigin(origins = "*") // 클라이언트 호스트 주소
@RestController
@RequestMapping("/api/recommendreply")
public class RecommendReplyController {

    private final RecommendReplyService recommendReplyService;

    // 생성자
    public RecommendReplyController(RecommendReplyService recommendReplyService) {
        this.recommendReplyService = recommendReplyService;
    }

    // 새로운 후기 저장

//    @PostMapping
//    public ResponseEntity<RecommendReplyResponse> saveReply(@RequestBody RecommendReplyResponse recommendReplyResponse) {
//        RecommendReplyResponse savedReply = recommendReplyService.saveReply(recommendReplyResponse);
//        return ResponseEntity.ok(savedReply);
//    }
        @PostMapping("/saveRecommendReply")
        public ResponseEntity<RecommendReplyResponse> saveRecommendReply(
        @RequestParam("recommendReplyId") Long recommendReplyId,
        @RequestParam("recommendReply") String recommendReply,
        @RequestParam("recommendReplyStar") Long recommendReplyStar,
        @RequestParam("recommendReplyTagValue") List<String> recommendReplyTagValue,
        @RequestParam("recommendPlaceId") RecommendListPlace recommendPlaceId,
        @RequestParam("recommendFoodId") RecommendListFood recommendFoodId,
        @RequestParam("recommendHotelId") RecommendListHotel recommendHotelId,
        @RequestParam("recommendEventId") RecommendListEvent recommendEventId) {

    // Create the DTO from the request parameters
    RecommendReplyResponse recommendReplyResponse = new RecommendReplyResponse(
            recommendReplyId, recommendReply, recommendReplyStar, recommendReplyTagValue,
            recommendPlaceId, recommendFoodId, recommendHotelId, recommendEventId);

    // Assuming there's a service method to save this response
    RecommendReplyResponse savedReply = recommendReplyService.saveReply(recommendReplyResponse);

    return ResponseEntity.ok(savedReply);
}

    // 모든 후기 조회
    @GetMapping
    public ResponseEntity<List<ReadRecommendReplyAllResponse>> getAllReplies() {
        List<ReadRecommendReplyAllResponse> replies = recommendReplyService.getReplyAll();
        return ResponseEntity.ok(replies);
    }

}
