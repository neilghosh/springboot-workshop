package com.convergence.ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-Commerce Product API",
                version = "v1",
                description = "Workshop API for managing and enriching products"
        )
)
public class OpenApiConfig {
}
