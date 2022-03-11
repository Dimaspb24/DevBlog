package com.project.devblog.controller;

import com.project.devblog.dto.request.ArticleRequest;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.service.ArticleService;
import static java.time.LocalDateTime.now;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class UserArticleControllerTest {

    @Mock
    ArticleService articleService;
    @InjectMocks
    UserArticleController userArticleController;

    ArticleEntity articleEntity;
    OpenArticleResponse openArticleResponse;
    CloseArticleResponse closeArticleResponse;
    ArticleRequest articleRequest;

    @BeforeEach
    void setUp() {
        closeArticleResponse = CloseArticleResponse.builder()
                .id(42)
                .title("Title")
                .status("PUBLISHED")
                .description("Description")
                .rating(42.42)
                .publicationDate(now())
                .modificationDate(now())
                .authorId(UUID.randomUUID().toString())
                .nickname("Nickname")
                .photo("Photo")
                .tags(List.of(TagResponse.builder()
                        .id(42)
                        .name("Name")
                        .build()))
                .build();
        articleEntity = ArticleEntity.builder()
                .id(42)
                .title("Title")
                .body("Body")
                .status(StatusArticle.PUBLISHED)
                .description("Description")
                .author(UserEntity.builder()
                        .id(closeArticleResponse.getAuthorId())
                        .personalInfo(PersonalInfo.builder()
                                .nickname("Nickname")
                                .photo("Photo")
                                .build())
                        .build())
                .tags(List.of(TagEntity.builder()
                        .id(42)
                        .name("Name")
                        .build()))
                .publicationDate(now())
                .build();
        articleEntity.setModificationDate(now());
        openArticleResponse = OpenArticleResponse.builder()
                .id(42)
                .title("Title")
                .status("PUBLISHED")
                .description("Description")
                .rating(42.42)
                .publicationDate(now())
                .modificationDate(now())
                .authorId(closeArticleResponse.getAuthorId())
                .nickname("Nickname")
                .photo("Photo")
                .tags(List.of(TagResponse.builder()
                        .id(42)
                        .name("Name")
                        .build()))
                .body("Body")
                .build();
        articleRequest = ArticleRequest.builder()
                .title("Title")
                .body("Body")
                .status("PUBLISHED")
                .description("Description")
                .tags(List.of("Name"))
                .build();
    }

    @Test
    void createSuccessTest() {
        final var userId = UUID.randomUUID().toString();
        when(articleService.create(any(), any(), any(), any(), any(), any()))
                .thenReturn(articleEntity);

        final var response = userArticleController.create(userId, articleRequest);

        verify(articleService).create(any(), any(), any(), any(), any(), any());
        assertEquals(openArticleResponse.getTitle(), response.getTitle());
        assertEquals(openArticleResponse.getNickname(), response.getNickname());
        assertEquals(openArticleResponse.getStatus(), response.getStatus());
        assertEquals(openArticleResponse.getDescription(), response.getDescription());
    }

    @Test
    void findSuccessTest() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        when(articleService.find(any(), any())).thenReturn(articleEntity);

        final var response = userArticleController.find(userId, articleId);

        verify(articleService).find(any(), any());
        assertEquals(openArticleResponse.getTitle(), response.getTitle());
        assertEquals(openArticleResponse.getNickname(), response.getNickname());
        assertEquals(openArticleResponse.getStatus(), response.getStatus());
        assertEquals(openArticleResponse.getDescription(), response.getDescription());
    }

    @Test
    void findAllSuccessTest() {
        final var userId = UUID.randomUUID().toString();
        final var pageable = Pageable.ofSize(5);
        when(articleService.findAll(any(), any())).thenReturn(new PageImpl<>(List.of(articleEntity)));

        final var response = userArticleController.findAll(userId, pageable);

        verify(articleService).findAll(any(), any());
        assertEquals(1, response.getTotalElements());
        assertTrue(response.stream().findFirst().isPresent());
        assertEquals(response.stream().findFirst().get().getId(), closeArticleResponse.getId());
        assertEquals(response.stream().findFirst().get().getNickname(), closeArticleResponse.getNickname());
        assertEquals(response.stream().findFirst().get().getDescription(),
                closeArticleResponse.getDescription());
    }

    @Test
    void deleteSuccessTest() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        doNothing().when(articleService).delete(any(), any());

        userArticleController.delete(userId, articleId);

        verify(articleService).delete(any(), any());
    }

    @Test
    void enableSuccessTest() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var enabled = true;
        doNothing().when(articleService).enable(any(), any(), any());

        userArticleController.enable(userId, articleId, enabled);

        verify(articleService).enable(any(), any(), any());
    }

    @Test
    void updateSuccessTest() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        when(articleService.update(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(articleEntity);

        final var response = userArticleController.update(userId, articleId, articleRequest);

        verify(articleService).update(any(), any(), any(), any(), any(), any(), any());
        assertEquals(openArticleResponse.getTitle(), response.getTitle());
        assertEquals(openArticleResponse.getNickname(), response.getNickname());
        assertEquals(openArticleResponse.getStatus(), response.getStatus());
        assertEquals(openArticleResponse.getDescription(), response.getDescription());
    }
}
