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
            if (handlerMethod.getMethod().getName().equals("join")) { // Controller method name
                RequestBody requestBody = new RequestBody();
                Content content = new Content();
                MediaType mediaType = new MediaType();

                // Define the schema for multipart form data
                ObjectSchema schema = new ObjectSchema();
                schema.addProperties("member", new StringSchema().description("Member JSON as String"));
                schema.addProperties("files", new StringSchema().description("File data").type("string").format("binary"));

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
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", getSecurityScheme()))
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