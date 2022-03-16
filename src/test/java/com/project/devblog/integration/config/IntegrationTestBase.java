package com.project.devblog.integration.config;

import org.junit.jupiter.api.BeforeAll;

public abstract class IntegrationTestBase extends PostgresTestContainer {

    @BeforeAll
    public static void setup() {
        postgreDBContainer.start();
    }

}
