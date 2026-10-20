package com.dynamodb_springboot.dynamodb.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Backend APIs",
                version = "1.0",
                description = "APIs for managing entitis"),
        servers = {
                @Server(url = "http://localhost:8083", description = "Local Server")
        }
)
public class OpenApiConfig {
}
