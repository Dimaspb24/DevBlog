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
import static java.time.LocalDateTime.now;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
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
class UserCommentControllerTest {

    @Mock
    CommentService commentService;
    @InjectMocks
    UserCommentController controller;

    CommentResponse commentResponse;
    CommentEntity commentEntity;
    UserEntity author;
    UserEntity receiver;
    ArticleEntity articleEntity;
    CommentRequest commentRequest;

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
        when(commentService.create(any(), any(), any(), any())).thenReturn(commentEntity);

        final var response = controller.create(userId, articleId, commentRequest);

        verify(commentService).create(any(), any(), any(), any());
    }

    @Test
    void find() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var commentId = 42L;
        when(commentService.findByIdAndAuthorIdAndArticleId(any(), any(), any())).thenReturn(commentEntity);

        final var response = controller.find(userId, articleId, commentId);

        verify(commentService).findByIdAndAuthorIdAndArticleId(any(), any(), any());
    }

    @Test
    void findAll() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var pageable = Pageable.ofSize(5);
        when(commentService.findAllByAuthorIdAndArticleId(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(commentEntity)));

        final var response = controller.findAll(userId, articleId, pageable);

        verify(commentService).findAllByAuthorIdAndArticleId(any(), any(), any());
    }

    @Test
    void delete() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var commentId = 42L;
        doNothing().when(commentService).delete(any(), any(), any());

        controller.delete(userId, articleId, commentId);

        verify(commentService).delete(any(), any(), any());
    }

    @Test
    void enable() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var commentId = 42L;
        final var enabled = true;
        doNothing().when(commentService).enable(any(), any(), any(), any());

        controller.enable(userId, articleId, commentId, enabled);

        verify(commentService).enable(any(), any(), any(), any());
    }

    @Test
    void update() {
        final var userId = UUID.randomUUID().toString();
        final var articleId = 42;
        final var commentId = 42L;
        when(commentService.update(any(), any(), any(), any())).thenReturn(commentEntity);

        final var response = controller.update(userId, articleId, commentId, commentRequest);

        verify(commentService).update(any(), any(), any(), any());
    }
}