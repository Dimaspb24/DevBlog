package com.project.devblog.service;

import com.project.devblog.dto.request.ArticleRequest;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusArticle;
import static com.project.devblog.model.enums.StatusArticle.CREATED;
import static com.project.devblog.model.enums.StatusArticle.PUBLISHED;
import static com.project.devblog.model.enums.StatusArticle.valueOf;
import com.project.devblog.repository.ArticleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    ArticleRepository articleRepository;
    @Mock
    TagService tagService;
    @Mock
    UserService userService;
    @InjectMocks
    ArticleService articleService;
    @Captor
    ArgumentCaptor<ArticleEntity> argumentCaptorArticle;

    UserEntity user;
    ArticleEntity article;

    @BeforeEach
    void init() {
        user = UserEntity.builder()
                .id("generatedId")
                .role(Role.USER)
                .login("login@mail.ru")
                .password("encodedPassword")
                .verificationCode(UUID.randomUUID().toString())
                .build();
        article = ArticleEntity.builder()
                .id(1)
                .author(user)
                .body("Some body")
                .status(PUBLISHED)
                .title("Some title")
                .description("Some description")
                .publicationDate(LocalDateTime.now())
                .build();

    }

    @Test
    void findByArticleIdAndUserId() {
        doReturn(Optional.of(article)).when(articleRepository)
                .findByIdAndAuthorIdAndEnabledIsTrue(article.getId(), user.getId());
        articleService.findByAuthorIdAndArticleId(user.getId(), article.getId());
        verify(articleRepository).findByIdAndAuthorIdAndEnabledIsTrue(article.getId(), user.getId());
    }

    @Test
    void throwIfNotFoundByArticleIdAndUserId() {
        doReturn(Optional.empty()).when(articleRepository)
                .findByIdAndAuthorIdAndEnabledIsTrue(article.getId(), user.getId());
        assertThrows(NotFoundException.class, () -> articleService.findByAuthorIdAndArticleId(user.getId(), article.getId()));
        verify(articleRepository).findByIdAndAuthorIdAndEnabledIsTrue(article.getId(), user.getId());
    }

    @Test
    void findByArticleId() {
        doReturn(Optional.of(article)).when(articleRepository).findById(article.getId());
        articleService.findById(article.getId());
        verify(articleRepository).findById(article.getId());
    }

    @Test
    void throwIfNotFoundByFindByArticleId() {
        doReturn(Optional.empty()).when(articleRepository).findById(article.getId());
        assertThrows(NotFoundException.class, () -> articleService.findById(article.getId()));
        verify(articleRepository).findById(article.getId());
    }

    @Test
    void create() {
        List<String> tags = List.of("1", "2", "3");
        List<TagEntity> tagEntities = List.of(new TagEntity("1"), new TagEntity("2"), new TagEntity("3"));
        article.setPublicationDate(LocalDateTime.now());
        article.setTags(tagEntities);
        doReturn(user).when(userService).findById(user.getId());
        doReturn(tagEntities).when(tagService).createAndGetAllByName(tags);

        articleService.create(user.getId(), article.getTitle(), tags, article.getDescription(),
                article.getBody(), article.getStatus());

        verify(articleRepository).save(argumentCaptorArticle.capture());
        assertThat(argumentCaptorArticle.getValue()).isEqualTo(article);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void shouldUpdateEnabled(Boolean enabled) {
        doReturn(Optional.of(article)).when(articleRepository).findByIdAndAuthorId(article.getId(), user.getId());

        articleService.enable(user.getId(), article.getId(), enabled);

        verify(articleRepository).save(argumentCaptorArticle.capture());
        assertThat(argumentCaptorArticle.getValue().getEnabled()).isEqualTo(enabled);
    }

    @Test
    void shouldThrowIfNotSameArticle() {
        doReturn(Optional.empty()).when(articleRepository).findByIdAndAuthorId(article.getId(), user.getId());
        assertThrows(NotFoundException.class, () -> articleService.enable(user.getId(), article.getId(), false));
        verify(articleRepository).findByIdAndAuthorId(article.getId(), user.getId());
    }

    @Test
    void delete() {
        doReturn(Optional.of(article)).when(articleRepository).findByIdAndAuthorId(article.getId(), user.getId());
        articleService.delete(user.getId(), article.getId());
        verify(articleRepository).delete(article);
    }

    @Test
    void throwIfNotFoundByIdAndAuthorId() {
        doReturn(Optional.empty()).when(articleRepository).findByIdAndAuthorId(article.getId(), user.getId());
        assertThrows(NotFoundException.class, () -> articleService.delete(user.getId(), article.getId()));
        verify(articleRepository).findByIdAndAuthorId(article.getId(), user.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"CREATED", "PUBLISHED"})
    void shouldUpdateArticle(String status) {
        if (status.equals("PUBLISHED")) {
            article.setStatus(CREATED);
        }
        ArticleRequest articleRequest = new ArticleRequest("Test", "Test", status, "Test", List.of("1", "2", "3"));
        List<TagEntity> tagEntities = List.of(new TagEntity("1"), new TagEntity("2"), new TagEntity("3"));

        doReturn(Optional.of(article)).when(articleRepository).findByIdAndAuthorIdAndEnabledIsTrue(article.getId(), user.getId());
        doReturn(tagEntities).when(tagService).createAndGetAllByName(articleRequest.getTags());

        articleService.update(user.getId(), article.getId(), articleRequest.getTitle(), articleRequest.getTags(),
                articleRequest.getDescription(), articleRequest.getBody(), valueOf(articleRequest.getStatus()));

        verify(articleRepository).save(argumentCaptorArticle.capture());
        ArticleEntity actualArticle = argumentCaptorArticle.getValue();

        StatusArticle statusArticle = article.getStatus();
        if (status.equals(CREATED.name()) && statusArticle.equals(PUBLISHED)) {
            assertThat(actualArticle.getPublicationDate()).isNull();
        } else if (status.equals(PUBLISHED.name()) && statusArticle.equals(CREATED)) {
            assertThat(actualArticle.getPublicationDate()).isNotNull();
        }
        assertAll(
                () -> assertThat(actualArticle.getTitle()).isEqualTo(articleRequest.getTitle()),
                () -> assertThat(actualArticle.getBody()).isEqualTo(articleRequest.getBody()),
                () -> assertThat(actualArticle.getDescription()).isEqualTo(articleRequest.getDescription()),
                () -> assertThat(actualArticle.getStatus()).isEqualTo(valueOf(articleRequest.getStatus())),
                () -> assertThat(actualArticle.getTags()).hasSize(articleRequest.getTags().size())
        );
    }

    @Test
    void findAll() {
        doReturn(new PageImpl<>(List.of(article))).when(articleRepository).findByAuthorIdAndEnabledIsTrue(user.getId(), Pageable.unpaged());
        articleService.findAllEnabled(user.getId(), Pageable.unpaged());
        verify(articleRepository).findByAuthorIdAndEnabledIsTrue(user.getId(), Pageable.unpaged());
    }

    @Test
    void findArticlesBySubscriptions() {
        doReturn(new PageImpl<>(List.of(article))).when(articleRepository).findBySubscriptions(user.getId(), Pageable.unpaged());
        articleService.findBySubscriptions(user.getId(), Pageable.unpaged());
        verify(articleRepository).findBySubscriptions(user.getId(), Pageable.unpaged());
    }

    @Test
    void findAllWithParameters() {
        String titleContains = "java";
        String tagName = "maven";
        PageRequest pageRequest = PageRequest.of(0, 10);
        doReturn(new PageImpl<>(List.of(article), pageRequest, 1)).when(articleRepository).findByEnabledAndTagNameAndTitleContains(anyString(), anyString(), any(Pageable.class));

        articleService.findAllEnabled(titleContains, tagName, pageRequest);

        verify(articleRepository).findByEnabledAndTagNameAndTitleContains(anyString(), anyString(), any(Pageable.class));
    }
}