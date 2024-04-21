package ultimatum.project.domain.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ultimatum.project.domain.entity.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 채팅방을 개설한 회원

    private String chatRoomName;

    private String chatRoomContent;

    // ChatMessage 엔터티와 OneToMany 관계 설정
    // cascade = CascadeType.ALL로 설정하여 ChatRoom 삭제 시 관련 ChatMessage도 함께 삭제되도록 함
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;

    // 문자열로 태그 목록을 저장
    private String travelStyleTags;

    // JSON 형태의 태그 목록을 가져오는 getter
    public List<String> getTravelStyleTags() {
        if (this.travelStyleTags == null || this.travelStyleTags.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(this.travelStyleTags, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            // JSON 변환 실패 시 빈 리스트 반환
            return new ArrayList<>();
        }
    }

    // JSON 형태의 태그 목록을 설정하는 setter
    public void setTravelStyleTags(List<String> tags) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.travelStyleTags = mapper.writeValueAsString(tags);
        } catch (JsonProcessingException e) {
            this.travelStyleTags = "[]";
        }
    }

    @JsonIgnore
    public String getTravelStyleTagsAsString() {
        // DB 저장용 raw 문자열 태그 목록을 가져오는 추가 getter
        return this.travelStyleTags;
    }

}
