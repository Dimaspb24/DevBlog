package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    ArticleService articleService;
    @Mock
    UserService userService;
    @Mock
    UserArticleRepository userArticleRepository;
    @InjectMocks
    RatingService ratingService;
    @Captor
    ArgumentCaptor<UserArticleEntity> argumentCaptorUserArticle;

    UserEntity user;
    ArticleEntity article;
    UserArticleEntity rating;

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
        rating = UserArticleEntity.builder()
                .id(2L)
                .article(article)
                .user(user)
                .rating(10)
                .build();
    }

    @Test
    void create() {
        doReturn(Optional.of(rating)).when(userArticleRepository).findByUserIdAndArticleIdAndArticleEnabledIsTrue(user.getId(), article.getId());
        doReturn(rating).when(userArticleRepository).save(any(UserArticleEntity.class));

        ratingService.createOrUpdate(rating.getUser().getId(), rating.getArticle().getId(), rating.getRating());

        verify(userArticleRepository).save(argumentCaptorUserArticle.capture());
        assertThat(argumentCaptorUserArticle.getValue().getRating()).isEqualTo(rating.getRating());
    }

    @Test
    void find() {
        doReturn(Optional.of(rating)).when(userArticleRepository).findByUserIdAndArticleIdAndArticleEnabledIsTrue(user.getId(), article.getId());
        ratingService.findByUserIdAndArticleId(user.getId(), article.getId());
        verify(userArticleRepository).findByUserIdAndArticleIdAndArticleEnabledIsTrue(user.getId(), article.getId());
    }

    @Test
    void throwIfNotFound() {
        doReturn(Optional.empty()).when(userArticleRepository).findByUserIdAndArticleIdAndArticleEnabledIsTrue(user.getId(), article.getId());
        assertThrows(NotFoundException.class, () -> ratingService.findByUserIdAndArticleId(user.getId(), article.getId()));
        verify(userArticleRepository).findByUserIdAndArticleIdAndArticleEnabledIsTrue(user.getId(), article.getId());
    }

    @Test
    void update() {
        int newRating = 6;
        doReturn(Optional.of(rating)).when(userArticleRepository).findByUserIdAndArticleIdAndArticleEnabledIsTrue(user.getId(), article.getId());

        ratingService.createOrUpdate(user.getId(), article.getId(), newRating);

        verify(userArticleRepository).findByUserIdAndArticleIdAndArticleEnabledIsTrue(user.getId(), article.getId());
        verify(userArticleRepository).save(argumentCaptorUserArticle.capture());
        assertThat(argumentCaptorUserArticle.getValue().getRating()).isEqualTo(newRating);
    }
}