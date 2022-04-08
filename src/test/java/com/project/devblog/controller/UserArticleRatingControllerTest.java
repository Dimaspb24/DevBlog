package com.project.devblog.controller;

import com.project.devblog.dto.request.RatingRequest;
import com.project.devblog.dto.response.RatingResponse;
import com.project.devblog.model.*;
import com.project.devblog.model.enums.BookmarkType;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.service.RatingService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class UserArticleRatingControllerTest {

    @Mock
    RatingService ratingService;
    @InjectMocks
    UserArticleRatingController userArticleRatingController;

    ArticleEntity articleEntity;
    UserArticleEntity userArticleEntity;
    UserEntity userEntity;
    RatingResponse ratingResponse;

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
        final var ratingRequest = RatingRequest.builder().rating(422).build();
        when(ratingService.createOrUpdate(any(), any(), any())).thenReturn(userArticleEntity);

        final var response = userArticleRatingController.createOrUpdate(userId, articleId, ratingRequest);

        verify(ratingService).createOrUpdate(any(), any(), any());
        assertEquals(ratingResponse.getRating(), response.getRating());
        assertEquals(ratingResponse.getArticleId(), response.getArticleId());
        assertEquals(ratingResponse.getAuthorId(), response.getAuthorId());
    }

    @Test
    void find() {
        final var userId = userEntity.getId();
        final var articleId = articleEntity.getId();
        when(ratingService.findByUserIdAndArticleId(any(), any())).thenReturn(userArticleEntity);

        final var response = userArticleRatingController.find(userId, articleId);

        verify(ratingService).findByUserIdAndArticleId(any(), any());
        assertEquals(ratingResponse.getRating(), response.getRating());
        assertEquals(ratingResponse.getArticleId(), response.getArticleId());
        assertEquals(ratingResponse.getAuthorId(), response.getAuthorId());
    }

    @Test
    void update() {
        final var userId = userEntity.getId();
        final var articleId = articleEntity.getId();
        final var ratingRequest = RatingRequest.builder().rating(422).build();
        when(ratingService.createOrUpdate(any(), any(), any())).thenReturn(userArticleEntity);

        final var response = userArticleRatingController.createOrUpdate(userId, articleId, ratingRequest);

        verify(ratingService).createOrUpdate(any(), any(), any());
        assertEquals(ratingResponse.getRating(), response.getRating());
        assertEquals(ratingResponse.getArticleId(), response.getArticleId());
        assertEquals(ratingResponse.getAuthorId(), response.getAuthorId());
    }
}