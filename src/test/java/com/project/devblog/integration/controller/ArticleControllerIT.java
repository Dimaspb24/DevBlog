package com.project.devblog.integration.controller;

import com.project.devblog.controller.ArticleController;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.CommentResponse;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.integration.config.annotation.IT;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.testcontainers.PostgresTestContainer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.project.devblog.integration.CommonData.ARTICLE_ID;
import static com.project.devblog.integration.CommonData.INVALID_ARTICLE_ID;
import static com.project.devblog.integration.CommonData.getExistsOpenArticleResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class ArticleControllerIT extends PostgresTestContainer {

    ArticleController articleController;

    @Test
    void find_shouldFindArticle_whenValidId() {
        OpenArticleResponse expectedOpenArticleResponse = getExistsOpenArticleResponse().build();

        OpenArticleResponse actualArticle = articleController.find(ARTICLE_ID);

        assertThat(actualArticle).isEqualTo(expectedOpenArticleResponse);
    }

    @Test
    void find_shouldThrowException_whenInvalidId() {
        assertThatThrownBy(() -> articleController.find(INVALID_ARTICLE_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ArticleEntity with id=100 not found");
    }

    @Test
    void findAll_shouldFindArticles_withNeededPaginationAndSorting() {
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
    void findAll_shouldFindArticlesByTitleContains() {
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
        CommentResponse expectedCommentResponse1 = CommentResponse.builder()
                .id(2L)
                .articleId(1)
                .authorNickname("Шрэк")
                .receiverNickname("Вождь")
                .message("Best title")
                .creationDate(LocalDateTime.now())
                .build();
        CommentResponse expectedCommentResponse2 = CommentResponse.builder()
                .id(3L)
                .articleId(1)
                .authorNickname("Вождь")
                .receiverNickname("Шрэк")
                .message("Oh no, you are exaggerating")
                .creationDate(LocalDateTime.now())
                .build();

        List<CommentResponse> actualCommentsResponses = articleController.findAllCommentsById(
                ARTICLE_ID, Pageable.unpaged()).getContent();

        assertThat(actualCommentsResponses).containsExactly(expectedCommentResponse1, expectedCommentResponse2);
    }
}
