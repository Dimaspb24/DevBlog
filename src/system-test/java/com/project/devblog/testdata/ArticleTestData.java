package com.project.devblog.testdata;


import com.project.devblog.dto.request.ArticleRequest;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.model.enums.StatusArticle;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArticleTestData {

    public static ArticleRequest.ArticleRequestBuilder getNewArticleRequest() {
        return ArticleRequest.builder()
                .title("System test title")
                .body("Body")
                .status(StatusArticle.PUBLISHED.name())
                .description("Some system test description");
    }
}
