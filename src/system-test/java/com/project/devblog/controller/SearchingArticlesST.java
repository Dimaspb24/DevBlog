package com.project.devblog.controller;

import com.project.devblog.dto.response.BookmarkArticleResponse;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.utils.ResponsePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

class SearchingArticlesST extends BaseST {

    @Test
    @DisplayName("Searching articles by tagName, titleContains, bookmarks and subscriptions")
    void searchArticles() {
        ResponsePage<CloseArticleResponse> closeArticleResponse;
        ResponsePage<BookmarkArticleResponse> bookmarkArticleResponse;
        HttpEntity<Void> entity = getHttpEntity(authResponseEntity);
        ParameterizedTypeReference<ResponsePage<CloseArticleResponse>> ptrCloseArticle = new ParameterizedTypeReference<>() {
        };
        ParameterizedTypeReference<ResponsePage<BookmarkArticleResponse>> ptrBookmarkArticle = new ParameterizedTypeReference<>() {
        };


        closeArticleResponse = restTemplate.exchange(basePathV1 + "/articles?tagName=docker", GET,
                entity, ptrCloseArticle).getBody();

        assertThat(closeArticleResponse).isNotNull();
        assertThat(closeArticleResponse.getContent()).hasSize(3);


        closeArticleResponse = restTemplate.exchange(basePathV1 + "/articles?titleContains=Spring", GET,
                entity, ptrCloseArticle).getBody();

        assertThat(closeArticleResponse).isNotNull();
        assertThat(closeArticleResponse.getContent()).hasSize(2);


        closeArticleResponse = restTemplate.exchange(basePathV1 + "/articles?titleContains=Spring&tagName=docker", GET,
                entity, ptrCloseArticle).getBody();

        assertThat(closeArticleResponse).isNotNull();
        assertThat(closeArticleResponse.getContent()).hasSize(2);


        bookmarkArticleResponse = restTemplate.exchange(basePathV1 + "/users/1/bookmarks", GET,
                entity, ptrBookmarkArticle).getBody();

        assertThat(bookmarkArticleResponse).isNotNull();
        assertThat(bookmarkArticleResponse.getContent()).hasSize(4);


        closeArticleResponse = restTemplate.exchange(basePathV1 + "/users/1/subscriptions/articles", GET,
                entity, ptrCloseArticle).getBody();

        assertThat(closeArticleResponse).isNotNull();
        assertThat(closeArticleResponse.getContent()).hasSize(1);
    }
}
