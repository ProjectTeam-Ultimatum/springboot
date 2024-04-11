package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ultimatum.project.dto.jejuAPI.JejuAllResponse;
import org.springframework.http.HttpStatus;

@Tag(name = "recommendList", description = "제주도API")
@Log4j2
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/jejuapi")
public class JejuApiController {

    private final WebClient webClient;

    public JejuApiController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.visitjeju.net").build();
    }

    @GetMapping("/all")
    public Mono<JejuAllResponse> getSights(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "pageSize", defaultValue = "12") int pageSize)
    {
        String apiKey = "nc3v2w57zkiafreu"; // 실제 API 키를 여기에 입력하세요.
        String locale = "kr";
        //String cid = "CONT_000000000500349";

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/vsjApi/contents/searchList")
                        .queryParam("apiKey", apiKey)
                        .queryParam("locale", locale)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize) // 변경된 페이지 사이즈 파라미터
                        //.queryParam("cid", cid)
                        .build())
                .retrieve()
                .onStatus(httpStatus -> httpStatus.isError(), clientResponse -> {
                    // 이 안에서 오류 처리를 합니다.
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorMessage -> Mono.error(new Exception(errorMessage)));
                })
                .bodyToMono(JejuAllResponse.class); // 수정된 DTO 참조
    }
}
