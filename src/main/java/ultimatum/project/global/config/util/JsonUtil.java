package ultimatum.project.global.config.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class JsonUtil {

    // ObjectMapper 인스턴스: 전역적으로 재사용하기 위해 static으로 선언.
    private static final ObjectMapper mapper = new ObjectMapper();

    // List<String>을 JSON 문자열로 변환합니다.
    public static String toJson(List<String> data) {
        try {
            // Java 객체를 JSON 문자열로 직렬화
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            // 직렬화 실패 시 빈 배열 "[]" 반환
            return "[]";
        }
    }

    // JSON 문자열을 List<String>으로 변환합니다.
    public static List<String> fromJson(String json) {
        if (json == null || json.isEmpty()) {
            // 입력 값이 null이나 비어있으면 빈 리스트 반환
            return Collections.emptyList();
        }
        try {
            // JSON 문자열을 Java 객체로 역직렬화
            return mapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            // 역직렬화 실패 시 빈 리스트 반환
            return Collections.emptyList();
        }
    }
}
