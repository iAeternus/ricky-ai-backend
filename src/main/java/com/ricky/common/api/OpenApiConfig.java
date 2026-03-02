package com.ricky.common.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Ricky AI Backend",
                version = "v1",
                description = "AI Chat Backend APIs"
        )
)
public class OpenApiConfig {
}
