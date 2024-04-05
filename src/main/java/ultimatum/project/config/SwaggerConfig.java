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
        // path : controller 가 속해있는 폴더경로 입력
        String[] path = {
                "ultimatum.project.chatting.ChatController"
        };
        return GroupedOpenApi.builder()
                .group("1.Review")
                .packagesToScan(path)
                .build();
    }

    @Bean
    public GroupedOpenApi secondOpenApi(){
        String[] path = {
                "ultimatum.project.chatting"
        };
        return GroupedOpenApi.builder()
                .group("2. 채팅")
                .packagesToScan(path)
                .build();
    }
}
