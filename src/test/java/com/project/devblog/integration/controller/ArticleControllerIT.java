package com.project.devblog.integration.controller;

import com.project.devblog.controller.ArticleController;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.CommentResponse;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.integration.config.annotation.IT;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.testcontainers.PostgresTestContainer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class ArticleControllerIT extends PostgresTestContainer {

    static final int VALID_ARTICLE_ID = 1;
    static final int INVALID_ARTICLE_ID = 100;
    static OpenArticleResponse expectedOpenArticleResponse;
    static CommentResponse expectedCommentResponse1;
    static CommentResponse expectedCommentResponse2;

    final ArticleController articleController;

    @BeforeAll
    static void init() {
        expectedOpenArticleResponse = OpenArticleResponse.builder()
                .id(1)
                .title("Spring Security with JWT")
                .body("Some body")
                .status(StatusArticle.PUBLISHED.name())
                .description("The Spring Security framework is ... ")
                .rating(2.0)
                .publicationDate(LocalDateTime.parse("2022-01-30T10:10:35"))
                .modificationDate(LocalDateTime.now())
                .authorId("1")
                .nickname("Вождь")
                .photo("https://histrf.ru/images/biographies/05/KuQYEI2HeM9mzCXevgKdebKS7FjiY87B1sm59t3t.jpg")
                .tags(List.of(
                        new TagResponse(1, "software"),
                        new TagResponse(2, "docker"),
                        new TagResponse(8, "ACID")
                ))
                .build();
        expectedCommentResponse1 = CommentResponse.builder()
                .id(2L)
                .articleId(1)
                .authorNickname("Шрэк")
                .receiverNickname("Вождь")
                .message("Best title")
                .creationDate(LocalDateTime.now())
                .build();
        expectedCommentResponse2 = CommentResponse.builder()
                .id(3L)
                .articleId(1)
                .authorNickname("Вождь")
                .receiverNickname("Шрэк")
                .message("Oh no, you are exaggerating")
                .creationDate(LocalDateTime.now())
                .build();
    }

    @Test
    void find_shouldFindArticle_whenValidId() {
        OpenArticleResponse actualArticle = articleController.find(VALID_ARTICLE_ID);
        assertThat(actualArticle).isEqualTo(expectedOpenArticleResponse);
    }

    @Test
    void find_shouldThrowException_whenInvalidId() {
        assertThatThrownBy(() -> articleController.find(INVALID_ARTICLE_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ArticleEntity with id=100 not found");
    }

    @Test
    void findAll_shouldFindArticlesWithNeededPagination() {
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "rating"));


        Page<CloseArticleResponse> closeArticleResponsePage = articleController.findAll(
                null, null, pageRequest);


        assertThat(closeArticleResponsePage.getPageable()).isEqualTo(pageRequest);

        List<CloseArticleResponse> content = closeArticleResponsePage.getContent();

        assertThat(content).hasSize(pageRequest.getPageSize())
                .extracting(CloseArticleResponse::getTitle)
                .containsExactly("Spring Security", "Title 4.2", "Title 6.3");
        assertThat(content).satisfiesExactly(
                article -> assertThat(article.getRating()).isEqualTo(8.0),
                article -> assertThat(article.getRating()).isEqualTo(8.0),
                article -> assertThat(article.getRating()).isEqualTo(7.0)
        );
    }

    @Test
    void findAll_shouldFindArticlesWithRightTitleContains() {
        String titleContains = "Title";
        List<CloseArticleResponse> actualArticleResponses = articleController.findAll(
                titleContains, null, Pageable.unpaged()).getContent();

        assertThat(actualArticleResponses).hasSize(4)
                .allMatch(art -> art.getStatus().equals(StatusArticle.PUBLISHED.name())
                        && Objects.nonNull(art.getPublicationDate())
                        && art.getTitle().contains(titleContains));
    }

    @Test
    void findAll_shouldFindArticlesByTagName() {
        String tagName = "maven";
        List<CloseArticleResponse> actualArticleResponses = articleController.findAll(
                null, tagName, Pageable.unpaged()).getContent();

        assertThat(actualArticleResponses).hasSize(2)
                .allMatch(art -> art.getStatus().equals(StatusArticle.PUBLISHED.name())
                        && Objects.nonNull(art.getPublicationDate())
                        && art.getTags().stream().anyMatch(tagResponse -> tagResponse.getName().equals(tagName)));
    }

    @Test
    void findAll_shouldFindArticlesByTitleContainsAndTagName() {
        String tagName = "maven";
        String titleContains = "Title 5";
        List<CloseArticleResponse> actualArticleResponses = articleController.findAll(
                titleContains, tagName, Pageable.unpaged()).getContent();

        assertThat(actualArticleResponses).hasSize(1)
                .allMatch(art -> art.getStatus().equals(StatusArticle.PUBLISHED.name())
                        && Objects.nonNull(art.getPublicationDate())
                        && art.getTitle().contains(titleContains)
                        && art.getTags().stream().anyMatch(tagResponse -> tagResponse.getName().equals(tagName)));
    }

    @Test
    void findAllCommentsById_shouldFindAllEnabledCommentsByValidArticleId() {
        List<CommentResponse> actualCommentsResponses = articleController.findAllCommentsById(
                VALID_ARTICLE_ID, Pageable.unpaged()).getContent();

        assertThat(actualCommentsResponses).containsExactly(expectedCommentResponse1, expectedCommentResponse2);
    }
}
