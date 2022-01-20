package com.project.devblog.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
//@SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
public class OpenApiConfig {

    @Bean
    public OpenAPI devBlogOpenApi(@Value("${application-description}") String description,
                                  @Value("${application-version}") String version) {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Application API")
                        .description(description)
                        .version(version)
                        .description("Description about Dev Blog")
                        .contact(new Contact()
                                .name("API Support")
                                .email("diman5178@gmail.com")))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Local service")))
                .externalDocs(new ExternalDocumentation()
                        .description("Dev Blog repository")
                        .url("https://github.com/Dimaspb24/DevBlog"))
//                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
//                .components(new Components()
//                        .addSecuritySchemes(securitySchemeName,
//                                new SecurityScheme()
//                                        .name(securitySchemeName)
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                        ))
                ;
    }
}
