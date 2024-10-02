package com.eager.questioncloud.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.List;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(@Value("v1.0") String appVersion) {
        Info info = new Info().title("QuestionCloud API Docs")
            .version(appVersion)
            .description("QuestionCloud API Docs");

        return new OpenAPI()
            .components(new Components())
            .info(info);
    }

    @Bean
    public OpenApiCustomizer removePageable() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> {
            pathItem.readOperationsMap().values().forEach(operation -> {
                List<Parameter> parameters = operation.getParameters();
                if (parameters != null) {
                    parameters.removeIf(param -> "pageable".equals(param.getName()));
                    parameters.removeIf(param -> "userId".equals(param.getName()));
                }
            });
        });
    }
}