package com.project.devblog.controller;

import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.CommentResponse;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.model.*;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.service.ArticleService;
import com.project.devblog.service.CommentService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    ArticleService articleService;
    @Mock
    CommentService commentService;
    @InjectMocks
    ArticleController articleController;

    CloseArticleResponse closeArticleResponse;
    OpenArticleResponse openArticleResponse;
    CommentResponse commentResponse;
    ArticleEntity articleEntity;
    CommentEntity commentEntity;

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
        commentResponse = CommentResponse.builder()
                .id(42L)
                .articleId(42)
                .authorNickname("AuthorNickname")
                .creationDate(now())
                .message("Message")
                .receiverNickname("ReceiverNickname")
                .build();
        commentEntity = CommentEntity.builder()
                .id(42L)
                .message("Message")
                .author(UserEntity.builder()
                        .id(closeArticleResponse.getAuthorId())
                        .personalInfo(PersonalInfo.builder()
                                .nickname("AuthorNickname")
                                .photo("Photo")
                                .build())
                        .build())
                .receiver(UserEntity.builder()
                        .id(closeArticleResponse.getAuthorId())
                        .personalInfo(PersonalInfo.builder()
                                .nickname("ReceiverNickname")
                                .photo("Photo")
                                .build())
                        .build())
                .article(articleEntity)
                .build();
        commentEntity.setCreationDate(now());
        commentEntity.setModificationDate(now());
    }

    @Test
    void findAllArticlesSuccessTest() {
        final var titleContains = "titleContains";
        final var tagName = "tagName";
        final var pageable = Pageable.ofSize(5);
        when(articleService.findAllEnabled(any(String.class), any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(articleEntity)));

        final var response = articleController.findAll(titleContains, tagName, pageable);

        verify(articleService).findAllEnabled(any(String.class), any(String.class), any(Pageable.class));
        assertEquals(1, response.getTotalElements());
        assertTrue(response.stream().findFirst().isPresent());
        assertEquals(response.stream().findFirst().get().getId(), closeArticleResponse.getId());
        assertEquals(response.stream().findFirst().get().getAuthorId(), closeArticleResponse.getAuthorId());
        assertEquals(response.stream().findFirst().get().getDescription(),
                closeArticleResponse.getDescription());
        assertEquals(response.stream().findFirst().get().getNickname(), closeArticleResponse.getNickname());
        assertEquals(response.stream().findFirst().get().getPhoto(), closeArticleResponse.getPhoto());
        assertEquals(response.stream().findFirst().get().getStatus(), closeArticleResponse.getStatus());
    }

    @Test
    void findArticleSuccessTest() {
        final var articleId = 42;
        when(articleService.findById(any(Integer.class))).thenReturn(articleEntity);

        final var response = articleController.find(articleId);

        verify(articleService).findById(any(Integer.class));
        assertEquals(openArticleResponse.getAuthorId(), response.getAuthorId());
        assertEquals(openArticleResponse.getDescription(), response.getDescription());
        assertEquals(openArticleResponse.getNickname(), response.getNickname());
        assertEquals(openArticleResponse.getBody(), response.getBody());
        assertEquals(openArticleResponse.getTitle(), response.getTitle());
        assertEquals(openArticleResponse.getPhoto(), response.getPhoto());
    }

    @Test
    void findAllCommentsSuccessTest() {
        final var articleId = 42;
        final var pageable = Pageable.ofSize(5);
        when(commentService.findAllByArticleId(any(Integer.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(commentEntity)));

        final var response = articleController.findAllCommentsById(articleId, pageable);

        verify(commentService).findAllByArticleId(any(Integer.class), any(Pageable.class));
        assertEquals(1, response.getTotalElements());
        assertTrue(response.stream().findFirst().isPresent());
        assertEquals(commentResponse.getAuthorNickname(),
                response.stream().findFirst().get().getAuthorNickname());
        assertEquals(commentResponse.getReceiverNickname(),
                response.stream().findFirst().get().getReceiverNickname());
        assertEquals(commentResponse.getArticleId(), response.stream().findFirst().get().getArticleId());
    }
}
