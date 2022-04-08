package com.project.devblog.controller;

import com.project.devblog.config.annotation.ST;
import com.project.devblog.dto.request.BookmarkRequest;
import com.project.devblog.dto.response.BookmarkArticleResponse;
import com.project.devblog.dto.response.BookmarkResponse;
import com.project.devblog.utils.ResponsePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@ST
public class UserBookmarkControllerST extends BaseAuthController {

    @Test
    @DisplayName("Добавление существующий статьи в закладки и дальнейшее удаление ее")
    void addingExistingArticleToBookmarkTest() {
        var entity = new HttpEntity<>(BookmarkRequest.builder()
                .bookmarkType("BOOKMARK")
                .build(), headers);
        var responseType = new ParameterizedTypeReference<BookmarkResponse>() {
        };
        var response = restTemplate.exchange(
                basePathV1 + "/users/2/articles/1/bookmarks",
                POST, entity, responseType);
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getArticleId()).isEqualTo(1);
        assertThat(response.getBody().getUserId()).isEqualTo("2");
        assertThat(response.getBody().getBookmarkType()).isEqualTo("BOOKMARK");

        var entity2 = new HttpEntity<>(null, headers);
        var responseType2 = new ParameterizedTypeReference<ResponsePage<BookmarkArticleResponse>>() {
        };
        var response2 = restTemplate.exchange(
                basePathV1 + "/users/2/bookmarks",
                GET, entity2, responseType2);
        assertThat(response2).isNotNull();
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().getContent()).isNotNull();
        assertTrue(response2.getBody().getContent().stream().
                anyMatch(bookmark -> bookmark.getArticleResponse().getId() == 1));

        restTemplate.exchange(
                basePathV1 + "/users/2/bookmarks/" + response2.getBody().getContent().stream()
                        .filter(bookmark -> bookmark.getArticleResponse().getId() == 1).findFirst().get().getId(),
                DELETE, entity, responseType);

        response2 = restTemplate.exchange(
                basePathV1 + "/users/2/bookmarks",
                GET, entity2, responseType2);
        assertThat(response2).isNotNull();
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().getContent()).isNotNull();
        assertFalse(response2.getBody().getContent().stream().
                anyMatch(bookmark -> bookmark.getArticleResponse().getId() == 1));
    }
}
