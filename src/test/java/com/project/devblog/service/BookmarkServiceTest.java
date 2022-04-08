package com.project.devblog.service;

import com.project.devblog.dto.request.BookmarkRequest;
import com.project.devblog.dto.response.BookmarkResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.BookmarkType;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.repository.UserArticleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    ArticleService articleService;
    @Mock
    UserService userService;
    @Mock
    UserArticleRepository userArticleRepository;
    @InjectMocks
    BookmarkService bookmarkService;
    @Captor
    ArgumentCaptor<UserArticleEntity> argumentCaptorUserArticle;

    UserEntity user;
    ArticleEntity article;
    UserArticleEntity bookmark;

    @BeforeEach
    void init() {
        user = UserEntity.builder()
                .id("generatedId")
                .role(Role.USER)
                .login("login@mail.ru")
                .password("encodedPassword")
                .build();
        user.setPersonalInfo(PersonalInfo.builder()
                .firstname("Ivan")
                .nickname("NickIvan")
                .phone("89999999999")
                .info("Some info")
                .lastname("Ivanov")
                .photo("https://...")
                .build());
        article = ArticleEntity.builder()
                .author(user)
                .id(1)
                .body("Some body")
                .status(StatusArticle.PUBLISHED)
                .title("Some title")
                .description("Some description")
                .publicationDate(LocalDateTime.now())
                .build();
        article.setModificationDate(LocalDateTime.now());
        bookmark = UserArticleEntity.builder()
                .id(2L)
                .article(article)
                .user(user)
                .bookmarkType(BookmarkType.BOOKMARK)
                .build();
    }

    @Test
    void shouldThrowWhenNotFoundById() {
        doReturn(Optional.empty()).when(userArticleRepository).findById(anyLong());
        assertThrows(NotFoundException.class, () -> bookmarkService.delete(1L));
    }

    @Test
    void shouldSaveWithNullBookmarkTypeWhenDeleteBookmark() {
        doReturn(Optional.of(bookmark)).when(userArticleRepository).findById(anyLong());

        bookmarkService.delete(1L);

        verify(userArticleRepository).findById(anyLong());
        verify(userArticleRepository).save(argumentCaptorUserArticle.capture());
        assertThat(argumentCaptorUserArticle.getValue().getBookmarkType()).isNull();
    }

    @Test
    void shouldFindAllBookmarks() {
        doReturn(new PageImpl<>(List.of(bookmark))).when(userArticleRepository).findByUserIdAndBookmarkTypeNotNull(anyString(), any(Pageable.class));
        bookmarkService.findAll("1", null, Pageable.unpaged());
        verify(userArticleRepository).findByUserIdAndBookmarkTypeNotNull(eq("1"), eq(Pageable.unpaged()));
    }

    @Test
    void shouldFindBookmarksByType() {
        doReturn(Page.empty()).when(userArticleRepository).findByUserIdAndBookmarkType(anyString(), any(), any(Pageable.class));
        bookmarkService.findAll("1", BookmarkType.BOOKMARK.name(), Pageable.unpaged());
        verify(userArticleRepository).findByUserIdAndBookmarkType(eq("1"), eq(BookmarkType.BOOKMARK), eq(Pageable.unpaged()));
    }

    @Test
    void saveBookmark() {
        doReturn(Optional.empty()).when(userArticleRepository).findByUserIdAndArticleId(anyString(), anyInt());
        doReturn(bookmark.getUser()).when(userService).findById(anyString());
        doReturn(bookmark.getArticle()).when(articleService).findById(anyInt());
        doReturn(bookmark).when(userArticleRepository).save(any(UserArticleEntity.class));
        BookmarkRequest request = new BookmarkRequest(BookmarkType.BOOKMARK.name());

        BookmarkResponse bookmarkResponseActual = bookmarkService.createOrUpdate(bookmark.getUser().getId(), bookmark.getArticle().getId(), request);

        BookmarkResponse bookmarkResponseExpected = new BookmarkResponse(user.getId(), article.getId(), bookmark.getBookmarkType().name());
        assertThat(bookmarkResponseActual.getUserId()).isEqualTo(bookmarkResponseExpected.getUserId());
        assertThat(bookmarkResponseActual.getArticleId()).isEqualTo(bookmarkResponseExpected.getArticleId());
        assertThat(bookmarkResponseActual.getBookmarkType()).isEqualTo(bookmarkResponseExpected.getBookmarkType());
    }

    @Test
    void updateBookmark() {
        doReturn(Optional.of(bookmark)).when(userArticleRepository).findByUserIdAndArticleId(anyString(), anyInt());
        doReturn(bookmark).when(userArticleRepository).save(any(UserArticleEntity.class));
        BookmarkRequest request = new BookmarkRequest(BookmarkType.FAVORITE.name());

        BookmarkResponse bookmarkResponseActual = bookmarkService.createOrUpdate(bookmark.getUser().getId(), bookmark.getArticle().getId(), request);

        BookmarkResponse bookmarkResponseExpected = new BookmarkResponse(user.getId(), article.getId(), bookmark.getBookmarkType().name());
        assertThat(bookmarkResponseActual.getUserId()).isEqualTo(bookmarkResponseExpected.getUserId());
        assertThat(bookmarkResponseActual.getArticleId()).isEqualTo(bookmarkResponseExpected.getArticleId());
        assertThat(bookmarkResponseActual.getBookmarkType()).isEqualTo(bookmarkResponseExpected.getBookmarkType());
    }
}