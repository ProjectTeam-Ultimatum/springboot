package ultimatum.project.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;

import java.util.List;

@Configuration
public class SwaggerConfig {


//    @Bean(name = "Review")
//    public GroupedOpenApi Review(){
//        return GroupedOpenApi.builder()
//                .group("사용자게시판")
//                .pathsToMatch("/api/reviews/**") // API 경로를 지정
//                .build();
//    }


    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = getSecurityScheme();
        SecurityRequirement securityRequirement = getSecurityRequirement();

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(List.of(securityRequirement))
                .info(apiInfo());
    }

    private Info apiInfo(){
        return new Info()
                .title("최후통첩 API Test")
                .description("최후통첩 팀의 제주랑 어플리케이션 API 입니다.")
                .version("1.0.0");
    }


    /** 보안 관련 헤더 추가를 위한 설정 */
    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement().addList("bearerAuth");
    }




}