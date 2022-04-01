package com.project.devblog.controller;

import com.project.devblog.config.annotation.ST;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.testcontainers.PostgresSTContainer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;

import static com.project.devblog.testdata.CommonData.INVALID_ARTICLE_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ST
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class ArticleControllerST extends PostgresSTContainer {

    ArticleController articleController;

    @Test
    void find_shouldThrowException_whenInvalidId() {
        assertThatThrownBy(() -> articleController.find(INVALID_ARTICLE_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ArticleEntity with id=100 not found");
    }
}
