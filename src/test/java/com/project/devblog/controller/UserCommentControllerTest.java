package com.project.devblog.controller;

import com.project.devblog.dto.request.CommentRequest;
import com.project.devblog.dto.response.CommentResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class UserCommentControllerTest {
    @Mock
    private CommentService commentService;
    @InjectMocks
    private UserCommentController controller;
    private CommentResponse commentResponse;
    private CommentEntity commentEntity;
    private UserEntity author;
    private UserEntity receiver;
    private ArticleEntity articleEntity;
    private CommentRequest commentRequest;

    @BeforeEach
    void setUp() {
        author = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .personalInfo(PersonalInfo.builder()
                        .nickname("Nickname")
                        .photo("Photo")
                        .build())
                .build();

        receiver = UserEntity.builder()
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
                .author(author)
                .tags(List.of(TagEntity.builder()
                        .id(42)
                        .name("Name")
                        .build()))
                .publicationDate(now())
                .build();
        articleEntity.setModificationDate(now());

        commentEntity = CommentEntity.builder()
                .id(42L)
                .message("Message")
                .enabled(true)
                .author(author)
                .receiver(receiver)
                .article(articleEntity)
                .build();
        commentEntity.setCreationDate(now());

        commentResponse = CommentResponse.builder()
                .authorNickname("authorNickname")
                .receiverNickname("receiverNickname")
                .articleId(articleEntity.getId())
                .creationDate(now())
                .message("Message")
                .id(42L)
                .build();

        commentRequest = CommentRequest.builder()
                .message("Message")
                .receiverId(receiver.getId())
                .build();
    }

    @Test
    void create() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        Mockito.when(commentService.create(any(), any(), any(), any()))
                .thenReturn(commentEntity);
        final var response = controller.create(userId, articleId, commentRequest);
        Mockito.verify(commentService).create(any(), any(), any(), any());
    }

    @Test
    void find() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var commentId = 42L;
        Mockito.when(commentService.find(any(), any(), any()))
                .thenReturn(commentEntity);
        final var response = controller.find(userId, articleId, commentId);
        Mockito.verify(commentService).find(any(), any(), any());
    }

    @Test
    void findAll() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var pageable = Pageable.ofSize(5);
        Mockito.when(commentService.findAllByAuthorIdAndArticleId(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(commentEntity)));
        final var response = controller.findAll(userId, articleId, pageable);
        Mockito.verify(commentService).findAllByAuthorIdAndArticleId(any(), any(), any());
    }

    @Test
    void delete() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var commentId = 42L;
        Mockito.doNothing().when(commentService).delete(any(), any(), any());
        controller.delete(userId, articleId, commentId);
        Mockito.verify(commentService).delete(any(), any(), any());
    }

    @Test
    void enable() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var commentId = 42L;
        final var enabled = true;
        Mockito.doNothing().when(commentService).enable(any(), any(), any(), any());
        controller.enable(userId, articleId, commentId, enabled);
        Mockito.verify(commentService).enable(any(), any(), any(), any());
    }

    @Test
    void update() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var commentId = 42L;
        Mockito.when(commentService.update(any(), any(), any(), any()))
                .thenReturn(commentEntity);
        final var response = controller.update(userId, articleId, commentId, commentRequest);
        Mockito.verify(commentService).update(any(), any(), any(), any());
    }
}