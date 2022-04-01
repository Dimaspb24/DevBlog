package com.project.devblog.controller;

import com.project.devblog.config.annotation.IT;
import com.project.devblog.dto.request.ArticleRequest;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.repository.CommentRepository;
import com.project.devblog.testcontainers.PostgresITContainer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.project.devblog.testdata.CommonData.*;
import static org.assertj.core.api.Assertions.*;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class UserArticleControllerIT extends PostgresITContainer {

    UserArticleController userArticleController;
    CommentRepository commentRepository;

    @Test
    void findAll_shouldFindAllUserArticles() {
        List<CloseArticleResponse> articlesByUserWithId6 = userArticleController.findAll("6", Pageable.unpaged()).getContent();

        assertThat(articlesByUserWithId6).hasSize(3);
    }

    @Test
    void create_shouldCreateNewArticle() {
        ArticleRequest newArticleRequest = getNewArticleRequest().build();

        OpenArticleResponse actualOpenArticleResponse = userArticleController.create(AUTHOR_ID, newArticleRequest);

        assertThat(actualOpenArticleResponse).satisfies(response -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getAuthorId()).isEqualTo(AUTHOR_ID);
            assertThat(response.getNickname()).isEqualTo(AUTHOR_NICKNAME);
            assertThat(response.getTitle()).isEqualTo(newArticleRequest.getTitle());
            assertThat(response.getBody()).isEqualTo(newArticleRequest.getBody());
            assertThat(response.getStatus()).isEqualTo(newArticleRequest.getStatus());
            assertThat(response.getDescription()).isEqualTo(newArticleRequest.getDescription());
            assertThat(response.getTags()).extracting(TagResponse::getName).containsAll(newArticleRequest.getTags());
            assertThat(response.getRating()).isNotNull();
            assertThat(response.getPhoto()).isNotNull();
            assertThat(response.getPublicationDate()).isNotNull();
            assertThat(response.getModificationDate()).isNotNull();
        });
    }

    @Test
    void find_shouldUserArticle() {
        OpenArticleResponse expectedOpenArticleResponse = getExistsOpenArticleResponse().build();

        OpenArticleResponse openArticleResponse = userArticleController.find(AUTHOR_ID, ARTICLE_ID);

        assertThat(openArticleResponse).isEqualTo(expectedOpenArticleResponse);
    }

    @Test
    void delete_shouldDeleteUserArticleAndAllCommentsThisArticle() {
        userArticleController.delete(AUTHOR_ID, ARTICLE_ID);

        assertThatThrownBy(() -> userArticleController.find(AUTHOR_ID, ARTICLE_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ArticleEntity was not found for parameters {articleId=1, authorId=1}");

        List<CommentEntity> allCommentsByArticleId = commentRepository
                .findAllByArticleIdAndEnabledIsTrue(ARTICLE_ID, Pageable.unpaged()).getContent();
        assertThat(allCommentsByArticleId).isEmpty();
    }

    @Test
    void enable_shouldBlockedArticle() {
        assertThatNoException().isThrownBy(() -> userArticleController.find(AUTHOR_ID, ARTICLE_ID));

        userArticleController.enable(AUTHOR_ID, ARTICLE_ID, false);

        assertThatThrownBy(() -> userArticleController.find(AUTHOR_ID, ARTICLE_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ArticleEntity was not found for parameters {articleId=1, authorId=1}");
    }

    @Test
    void update_shouldUpdateArticle_withValidRequest() {
        ArticleRequest newArticleRequest = getNewArticleRequest().build();

        OpenArticleResponse updatedArticleResponse = userArticleController.update(AUTHOR_ID, ARTICLE_ID, newArticleRequest);

        assertThat(updatedArticleResponse).satisfies(response -> {
            assertThat(response.getTitle()).isEqualTo(newArticleRequest.getTitle());
            assertThat(response.getBody()).isEqualTo(newArticleRequest.getBody());
            assertThat(response.getStatus()).isEqualTo(newArticleRequest.getStatus());
            assertThat(response.getDescription()).isEqualTo(newArticleRequest.getDescription());
            assertThat(response.getTags()).extracting(TagResponse::getName).containsAll(newArticleRequest.getTags());
        });
    }
}
