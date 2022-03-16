package com.project.devblog.integration.controller;

import com.project.devblog.controller.ArticleController;
import com.project.devblog.integration.config.IntegrationTestBase;
import com.project.devblog.integration.config.annotation.IT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class ArticleControllerTest extends IntegrationTestBase {

    final ArticleController articleController;

    @Test
    void findAll() {
        Assertions.assertDoesNotThrow(() -> articleController.find(1));
    }
}
