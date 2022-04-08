package com.project.devblog.controller;

import com.project.devblog.dto.request.BookmarkRequest;
import com.project.devblog.dto.response.BookmarkArticleResponse;
import com.project.devblog.dto.response.BookmarkResponse;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.service.BookmarkService;
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
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class UserBookmarkControllerTest {

    @Mock
    BookmarkService bookmarkService;
    @InjectMocks
    UserBookmarkController bookmarkController;

    BookmarkResponse bookmarkResponse;
    BookmarkArticleResponse bookmarkArticleResponse;
    BookmarkRequest bookmarkRequest;

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
        when(bookmarkService.createOrUpdate(any(), any(), any()))
                .thenReturn(bookmarkResponse);

        final var response = bookmarkController.createOrUpdate(userId, articleId, bookmarkRequest);

        verify(bookmarkService).createOrUpdate(any(), any(), any());
        assertEquals(bookmarkResponse.getUserId(), response.getUserId());
        assertEquals(bookmarkResponse.getBookmarkType(), response.getBookmarkType());
        assertEquals(bookmarkResponse.getArticleId(), response.getArticleId());
    }

    @Test
    void findAll() {
        final var userId = bookmarkResponse.getUserId();
        final var bookmarkType = bookmarkResponse.getBookmarkType();
        final var pageable = Pageable.ofSize(5);
        when(bookmarkService.findAll(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookmarkArticleResponse)));

        final var response = bookmarkController.findAll(userId, bookmarkType, pageable);

        verify(bookmarkService).findAll(any(), any(), any());
        assertEquals(1, response.getTotalElements());
        assertTrue(response.stream().findFirst().isPresent());
        assertEquals(bookmarkArticleResponse.getId(), response.stream().findFirst().get().getId());
        assertEquals(bookmarkArticleResponse.getBookmarkType(),
                response.stream().findFirst().get().getBookmarkType());
        assertEquals(bookmarkArticleResponse.getRating(), response.stream().findFirst().get().getRating());
    }

    @Test
    void delete() {
        final var userId = bookmarkResponse.getUserId();
        final var bookmarkId = 42L;
        doNothing().when(bookmarkService).delete(any());

        bookmarkController.delete(userId, bookmarkId);

        verify(bookmarkService).delete(any());
    }
}