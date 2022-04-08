package com.project.devblog.testdata;

import com.project.devblog.dto.request.ArticleRequest;
import com.project.devblog.dto.request.RatingRequest;
import com.project.devblog.model.enums.StatusArticle;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {

    public static ArticleRequest.ArticleRequestBuilder getArticleRequest() {
        return ArticleRequest.builder()
                .title("System test title")
                .body("Success")
                .status(StatusArticle.PUBLISHED.name())
                .description("Some system test description")
                .tags(List.of("System test", "Spring Boot", "Java"));
    }

    public static RatingRequest.RatingRequestBuilder getRatingRequest() {
        return RatingRequest.builder()
                .rating(4);
    }
}