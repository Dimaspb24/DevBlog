package com.project.devblog.testcontainers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerApplicationContextInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgresContainer.start();

        TestPropertyValues.of("spring.datasource.url=" + postgresContainer.getJdbcUrl())
                .applyTo(applicationContext.getEnvironment());
    }
}
