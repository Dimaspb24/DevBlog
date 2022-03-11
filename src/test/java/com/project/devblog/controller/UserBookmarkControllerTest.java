package com.project.devblog.controller;

import com.project.devblog.dto.request.BookmarkRequest;
import com.project.devblog.dto.response.BookmarkArticleResponse;
import com.project.devblog.dto.response.BookmarkResponse;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.service.BookmarkService;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserBookmarkControllerTest {
    @Mock
    private BookmarkService bookmarkService;
    @InjectMocks
    private UserBookmarkController bookmarkController;
    private BookmarkResponse bookmarkResponse;
    private BookmarkArticleResponse bookmarkArticleResponse;
    private BookmarkRequest bookmarkRequest;

    @BeforeEach
    void setUp() {
        final var closeArticleResponse = CloseArticleResponse.builder()
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

        bookmarkResponse = BookmarkResponse.builder()
                .userId(UUID.randomUUID().toString())
                .articleId(42)
                .bookmarkType("BOOKMARK")
                .build();

        bookmarkRequest = BookmarkRequest.builder()
                .bookmarkType("BOOKMARK")
                .build();

        bookmarkArticleResponse = BookmarkArticleResponse.builder()
                .id(42L)
                .rating(422)
                .bookmarkType("BOOKMARK")
                .articleResponse(closeArticleResponse)
                .build();
    }

    @Test
    void create() {
        final var userId = bookmarkResponse.getUserId();
        final var articleId = bookmarkResponse.getArticleId();
        Mockito.when(bookmarkService.create(any(), any(), any()))
                .thenReturn(bookmarkResponse);
        final var response = bookmarkController.create(userId, articleId, bookmarkRequest);
        Mockito.verify(bookmarkService).create(any(), any(), any());
        Assertions.assertEquals(bookmarkResponse.getUserId(), response.getUserId());
        Assertions.assertEquals(bookmarkResponse.getBookmarkType(), response.getBookmarkType());
        Assertions.assertEquals(bookmarkResponse.getArticleId(), response.getArticleId());
    }

    @Test
    void findAll() {
        final var userId = bookmarkResponse.getUserId();
        final var bookmarkType = bookmarkResponse.getBookmarkType();
        final var pageable = Pageable.ofSize(5);
        Mockito.when(bookmarkService.findAll(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookmarkArticleResponse)));
        final var response = bookmarkController.findAll(userId, bookmarkType, pageable);
        Mockito.verify(bookmarkService).findAll(any(), any(), any());
        Assertions.assertEquals(1, response.getTotalElements());
        Assertions.assertTrue(response.stream().findFirst().isPresent());
        Assertions.assertEquals(bookmarkArticleResponse.getId(), response.stream().findFirst().get().getId());
        Assertions.assertEquals(bookmarkArticleResponse.getBookmarkType(),
                response.stream().findFirst().get().getBookmarkType());
        Assertions.assertEquals(bookmarkArticleResponse.getRating(), response.stream().findFirst().get().getRating());
    }

    @Test
    void delete() {
        final var userId = bookmarkResponse.getUserId();
        final var bookmarkId = 42L;
        Mockito.doNothing().when(bookmarkService).delete(any());
        bookmarkController.delete(userId, bookmarkId);
        Mockito.verify(bookmarkService).delete(any());
    }
}