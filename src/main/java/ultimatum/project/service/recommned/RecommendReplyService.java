package ultimatum.project.service.recommned;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ultimatum.project.domain.dto.recommendReply.ReadRecommendReplyAllResponse;
import ultimatum.project.domain.dto.recommendReply.RecommendReplyResponse;
import ultimatum.project.domain.entity.recommendReply.RecommendReply;
import ultimatum.project.repository.reply.RecommendReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendReplyService {

    private final RecommendReplyRepository recommendReplyRepository;
    private final ModelMapper modelMapper;

    //후기 저장
    public RecommendReplyResponse saveReply(RecommendReplyResponse recommendReplyResponse) {
        RecommendReply recommendReply = recommendReplyRepository.findById(recommendReplyResponse.getRecommendReplyId())
                .orElseThrow(() -> new IllegalArgumentException("후기를 찾을 수 없습니다."));

        modelMapper.map(recommendReplyResponse, recommendReply);
        recommendReplyRepository.save(recommendReply);

        return modelMapper.map(recommendReply, RecommendReplyResponse.class);
    }

//    public void seveReply(RecommendReplyResponse recommendReplyResponse){
//        RecommendReply recommendReply = recommendReplyRepository.findById(recommendReplyResponse.getRecommendReplyId())
//                .orElseThrow(() -> new IllegalArgumentException("후기를 찾을 수 없습니다."));
//
//        RecommendReplyResponse response = new RecommendReplyResponse();
//        response.setRecommendReplyStar(recommendReplyResponse.getRecommendReplyStar());
//        response.setRecommendReplyTagValue(recommendReplyResponse.getRecommendReplyTagValue());
//        response.setRecommendFoodId(recommendReplyResponse.getRecommendFoodId());
//        response.setRecommendPlaceId(recommendReplyResponse.getRecommendPlaceId());
//        response.setRecommendHotelId(recommendReplyResponse.getRecommendHotelId());
//        response.setRecommendEventId(recommendReplyResponse.getRecommendEventId());
//    }

    // 후기 전체 조회
    // 후기 전체 조회
    public List<ReadRecommendReplyAllResponse> getReplyAll() {
        return recommendReplyRepository.findAll().stream()
                .map(recommendReply -> modelMapper.map(recommendReply, ReadRecommendReplyAllResponse.class))
                .collect(Collectors.toList());
    }

    //ID 조회



}
