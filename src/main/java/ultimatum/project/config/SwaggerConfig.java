package ultimatum.project.config;

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
        String[] path = {
                "org.example.ohgiraffers.board.controller.ReviewController"
        };
        return GroupedOpenApi.builder()
                .group("1.Review")
                .packagesToScan(path)
                .build();
    }

    @Bean
    public GroupedOpenApi secondOpenApi(){
        String[] path = {
                ""
        };
        return GroupedOpenApi.builder()
                .group("2. 아직 미정")
                .packagesToScan(path)
                .build();
    }
}
