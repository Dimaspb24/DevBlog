package com.project.devblog.integration.controller;

import com.project.devblog.controller.UserArticleController;
import com.project.devblog.integration.config.annotation.IT;
import com.project.devblog.testcontainers.PostgresTestContainer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class UserArticleControllerIT extends PostgresTestContainer {

    final UserArticleController userArticleController;

    @Test
    void findAll() {
//        Assertions.assertDoesNotThrow(() -> userArticleController.findAll("2", Pageable.unpaged()));
    }

    @Test
    void create() {
    }

    @Test
    void find() {
    }

    @Test
    void delete() {
    }

    @Test
    void enable() {
    }

    @Test
    void update() {
    }
}
