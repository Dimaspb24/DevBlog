package com.project.devblog;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class AbstractIT {

    @Container
    private static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>("postgres:13")
            .withUsername("demouser")
            .withPassword("demopassword");

    @DynamicPropertySource
    public static void overrideProds(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreDBContainer::getJdbcUrl);
    }
}
