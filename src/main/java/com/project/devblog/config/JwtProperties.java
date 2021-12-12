package com.project.devblog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.token")
@Getter
@Setter
public class JwtProperties {

    private String secret;
    private long expired;
}
