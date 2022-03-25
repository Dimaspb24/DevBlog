package com.project.devblog.integration;

import com.project.devblog.dto.request.ArticleRequest;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.model.enums.StatusArticle;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonData {

    public final static String AUTHOR_ID = "1";
    public final static String AUTHOR_LOGIN = "mail1@mail.ru";
    public final static String AUTHOR_NICKNAME = "Вождь";
    public final static int ARTICLE_ID = 1;
    public final static int INVALID_ARTICLE_ID = 100;

    public static OpenArticleResponse.OpenArticleResponseBuilder getExistsOpenArticleResponse() {
        return OpenArticleResponse.builder()
                .id(ARTICLE_ID)
                .authorId(AUTHOR_ID)
                .nickname(AUTHOR_NICKNAME)
                .photo("https://histrf.ru/images/biographies/05/KuQYEI2HeM9mzCXevgKdebKS7FjiY87B1sm59t3t.jpg")
                .title("Spring Security with JWT")
                .body("Some body")
                .status(StatusArticle.PUBLISHED.name())
                .description("The Spring Security framework is ... ")
                .rating(2.0)
                .publicationDate(LocalDateTime.parse("2022-01-30T10:10:35"))
                .modificationDate(LocalDateTime.now())
                .tags(List.of(
                        new TagResponse(1, "software"),
                        new TagResponse(2, "docker"),
                        new TagResponse(8, "ACID")
                ));
    }

    public static ArticleRequest.ArticleRequestBuilder getNewArticleRequest() {
        return ArticleRequest.builder()
                .title("Integration test title")
                .body("Success")
                .status(StatusArticle.PUBLISHED.name())
                .description("Some integration test description")
                .tags(List.of("Integration test", "Spring Boot", "Java"));
    }
}
