package com.project.devblog.controller;

import com.project.devblog.dto.request.RatingRequest;
import com.project.devblog.dto.response.RatingResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.BookmarkType;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.service.RatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserArticleRatingControllerTest {
    @Mock
    private RatingService ratingService;
    @InjectMocks
    private UserArticleRatingController userArticleRatingController;
    private ArticleEntity articleEntity;
    private UserArticleEntity userArticleEntity;
    private UserEntity userEntity;
    private RatingResponse ratingResponse;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .personalInfo(PersonalInfo.builder()
                        .nickname("Nickname")
                        .photo("Photo")
                        .build())
                .build();

        articleEntity = ArticleEntity.builder()
                .id(42)
                .title("Title")
                .body("Body")
                .status(StatusArticle.PUBLISHED)
                .description("Description")
                .author(userEntity)
                .tags(List.of(TagEntity.builder()
                        .id(42)
                        .name("Name")
                        .build()))
                .publicationDate(now())
                .build();
        articleEntity.setModificationDate(now());

        userArticleEntity = UserArticleEntity.builder()
                .id(42L)
                .rating(422)
                .user(userEntity)
                .article(articleEntity)
                .bookmarkType(BookmarkType.BOOKMARK)
                .build();

        ratingResponse = RatingResponse.builder()
                .authorId(userEntity.getId())
                .articleId(articleEntity.getId())
                .rating(userArticleEntity.getRating())
                .build();
    }

    @Test
    void create() {
        final var userId = userEntity.getId();
        final var articleId = articleEntity.getId();
        final var ratingRequest = RatingRequest.builder()
                .rating(422)
                .build();
        Mockito.when(ratingService.create(any(), any(), any()))
                .thenReturn(userArticleEntity);
        final var response = userArticleRatingController.create(userId, articleId, ratingRequest);
        Mockito.verify(ratingService).create(any(), any(), any());
        Assertions.assertEquals(ratingResponse.getRating(), response.getRating());
        Assertions.assertEquals(ratingResponse.getArticleId(), response.getArticleId());
        Assertions.assertEquals(ratingResponse.getAuthorId(), response.getAuthorId());
    }

    @Test
    void find() {
        final var userId = userEntity.getId();
        final var articleId = articleEntity.getId();
        Mockito.when(ratingService.find(any(), any()))
                .thenReturn(userArticleEntity);
        final var response = userArticleRatingController.find(userId, articleId);
        Mockito.verify(ratingService).find(any(), any());
        Assertions.assertEquals(ratingResponse.getRating(), response.getRating());
        Assertions.assertEquals(ratingResponse.getArticleId(), response.getArticleId());
        Assertions.assertEquals(ratingResponse.getAuthorId(), response.getAuthorId());
    }

    @Test
    void update() {
        final var userId = userEntity.getId();
        final var articleId = articleEntity.getId();
        final var ratingRequest = RatingRequest.builder()
                .rating(422)
                .build();
        Mockito.when(ratingService.update(any(), any(), any()))
                .thenReturn(userArticleEntity);
        final var response = userArticleRatingController.update(userId, articleId, ratingRequest);
        Mockito.verify(ratingService).update(any(), any(), any());
        Assertions.assertEquals(ratingResponse.getRating(), response.getRating());
        Assertions.assertEquals(ratingResponse.getArticleId(), response.getArticleId());
        Assertions.assertEquals(ratingResponse.getAuthorId(), response.getAuthorId());
    }
}