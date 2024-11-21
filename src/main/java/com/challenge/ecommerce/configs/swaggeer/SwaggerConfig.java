package com.challenge.ecommerce.configs.swaggeer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {
  @Bean
  GroupedOpenApi groupedOpenApi() {
    return GroupedOpenApi.builder().group("public-apis").pathsToMatch("/**").build();
  }

  @Bean
  OpenAPI customOpenAPI(ServletContext servletContext) {
    Server server = new Server().url(servletContext.getContextPath());
    return new OpenAPI()
        .info(new Info().title("API book storage online").version("1.0"))
        .servers(List.of(server))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
  }
}
