package ultimatum.project.global.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OperationCustomizer;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi customOpenApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .addOperationCustomizer(customizeFileUploadOperation())
                .pathsToMatch("/api/**")
                .build();
    }

    private OperationCustomizer customizeFileUploadOperation() {
        return (operation, handlerMethod) -> {
            if (handlerMethod.getMethod().getName().equals("join")) {
                // 'join' 메서드의 경우 파일 업로드 요청 스키마 정의
                RequestBody requestBody = new RequestBody();
                Content content = new Content();
                MediaType mediaType = new MediaType();

                ObjectSchema schema = new ObjectSchema();
                schema.addProperties("member", new StringSchema().description("Member JSON as String"));
                schema.addProperties("files", new StringSchema().description("File data").type("string").format("binary"));

                mediaType.setSchema(schema);
                content.addMediaType("multipart/form-data", mediaType);
                requestBody.setContent(content);
                requestBody.setRequired(true);
                operation.setRequestBody(requestBody);
            } else if (handlerMethod.getMethod().getName().equals("updateProfileImage")) {
                // 'updateProfileImage' 메서드의 경우 파일 업로드 요청 스키마 정의
                RequestBody requestBody = new RequestBody();
                Content content = new Content();
                MediaType mediaType = new MediaType();

                // 파일 업로드 요청 스키마 정의
                ObjectSchema schema = new ObjectSchema();
                schema.addProperties("imageFile", new StringSchema().type("string").format("binary"));

                mediaType.setSchema(schema);
                content.addMediaType("multipart/form-data", mediaType);
                requestBody.setContent(content);
                requestBody.setRequired(true);
                operation.setRequestBody(requestBody);
            }

            return operation;
        };
    }


    @Bean
    public OpenAPI openAPI() {

        Server server = new Server();
        server.setUrl("https://jejurang.site"); // https://에 접근 가능하게 설정

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", getSecurityScheme()))
                .servers(List.of(server))
                .security(List.of(getSecurityRequirement()))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("최후통첩 API Test")
                .description("최후통첩 팀의 제주랑 어플리케이션 API 입니다.")
                .version("1.0.0");
    }

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