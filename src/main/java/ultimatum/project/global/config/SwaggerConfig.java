package ultimatum.project.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "Project Team - Ultimatum",
                description = "수료전 우리의 최후통첩을 전달합시다",
                version = "v1")
)
@Configuration
public class SwaggerConfig {


    @Bean
    public GroupedOpenApi Review(){
        return GroupedOpenApi.builder()
                .group("사용자게시판")
                .pathsToMatch("/api/reviews/**") // API 경로를 지정
                .build();
    }

    @Bean
    public GroupedOpenApi thirdOpenApi(){
        String[] path = {
                "ultimatum.project.controller"
        };
        return GroupedOpenApi.builder()
                .group("3. 전역예외처리")
                .packagesToScan(path)
                .build();
    }
}
