package com.eager.questioncloud.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
}