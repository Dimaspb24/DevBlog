package com.project.devblog.config;

import com.project.devblog.constant.OpenApiConstant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.project.devblog.constant.OpenApiConstant.*;
import static java.lang.String.format;

@Configuration
public class OpenApiConfig {

    @Value("${info.app.version}")
    private String apiVersion;

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi
                .builder()
                .group(format("Api %s", apiVersion))
                .pathsToMatch(format("/%s/**", apiVersion))
                .build();
    }

    @Bean
    public GroupedOpenApi actuatorGroup() {
        return GroupedOpenApi
                .builder()
                .group("Metrics")
                .pathsToMatch("/actuator/**")
                .build();
    }

    @Bean
    public OpenAPI devBlogOpenApi() {
        return new OpenAPI()
                .info(getInfo(apiVersion))
                .servers(List.of(getLocalService()))
                .externalDocs(getDocumentation())
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SECURITY_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SECURITY_SCHEME, getSecuritySchemeJWT())
                );
    }

    private SecurityScheme getSecuritySchemeJWT() {
        return new SecurityScheme()
                .name(BEARER_SECURITY_SCHEME)
                .scheme(BEARER_SCHEME_NAME)
                .bearerFormat(BEARER_FORMAT)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER);
    }

    private ExternalDocumentation getDocumentation() {
        return new ExternalDocumentation()
                .url(URL_DOC)
                .description(DESCRIPTION_DOC);
    }

    private Server getLocalService() {
        return new Server()
                .url(URL_LOCAL_SERVER)
                .description(DESCRIPTION_LOCAL_SERVER);
    }

    private Info getInfo(String version) {
        return new Info()
                .version(version)
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .description(OpenApiConstant.API_DESCRIPTION)
                .contact(getContact());
    }

    private Contact getContact() {
        return new Contact()
                .name(CONTACT_NAME)
                .email(CONTACT_EMAIL);
    }
}
