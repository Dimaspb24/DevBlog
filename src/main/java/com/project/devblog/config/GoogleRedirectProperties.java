package com.project.devblog.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.google.redirect")
public class GoogleRedirectProperties {
    @NonNull
    private String url;
}
