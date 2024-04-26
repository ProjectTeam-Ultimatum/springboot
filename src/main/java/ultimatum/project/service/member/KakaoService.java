package ultimatum.project.service.member;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ultimatum.project.domain.dto.logInDTO.KakaoUserInfoDTO1;
import ultimatum.project.domain.dto.logInDTO.KakaoUserInfoDto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Log4j2
public class KakaoService {

    public String getKakaoAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String requestURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            // setDoOutput()은 OutputStream으로 POST 데이터를 넘겨 주겠다는 옵션이다.
            // POST 요청을 수행하려면 setDoOutput()을 true로 설정한다.
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=100bde42d2ac4c0bf9cf54655e5395cc" + // REST_API_KEY
                    "&redirect_uri=http://localhost:8081/social" + // REDIRECT_URI
                    "&code=" + code;
            bufferedWriter.write(sb);
            bufferedWriter.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 데이터를 InputStreamReader을 통해 읽어 오기
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);

            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public KakaoUserInfoDTO1 getKakaoUserInfo(String accessToken) {
        String requestUrl = "https://kapi.kakao.com/v2/user/me";
        log.info("requestUrl : {}",requestUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        log.info("headers : {}",headers);
        log.info("entity : {}",entity);



        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoUserInfoDTO1> response = restTemplate.exchange(
                requestUrl, HttpMethod.GET, entity, KakaoUserInfoDTO1.class
        );
        log.info("response:{}",response);

        KakaoUserInfoDTO1 kakaoUserInfoDto = response.getBody();
        if (kakaoUserInfoDto != null) {
            String nickname = kakaoUserInfoDto.getProperties() != null ? kakaoUserInfoDto.getProperties().getNickname() : null;
            String email = kakaoUserInfoDto.getKakao_account() != null ? kakaoUserInfoDto.getKakao_account().getEmail() : null;
            log.info("Kakao User Nickname: {}", nickname);
            log.info("Kakao User Email: {}", email);

            // 추가적인 로직...
        } else {
            log.error("Kakao User Info is null");
            // 적절한 예외 처리를 수행하거나 오류 응답 반환
        }

        return kakaoUserInfoDto;
    }

}
