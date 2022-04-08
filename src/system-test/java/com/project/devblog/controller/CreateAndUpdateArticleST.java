package com.project.devblog.controller;

import com.project.devblog.config.annotation.ST;
import com.project.devblog.dto.request.ArticleRequest;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.utils.ResponsePage;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;

import java.util.List;

import static com.project.devblog.testdata.TestData.getArticleRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@ST
class CreateAndUpdateArticleST extends BaseST {

    @Test
    void updateArticle() {
        ArticleRequest request = getArticleRequest().build();

        OpenArticleResponse responseBefore = restTemplate.exchange(basePathV1 + "/users/1/articles/1",
                GET, getHttpEntity(authResponseEntity), OpenArticleResponse.class).getBody();
        restTemplate.exchange(basePathV1 + "/users/1/articles/1", PUT, getHttpEntity(request, authResponseEntity), OpenArticleResponse.class);
        OpenArticleResponse responseAfter = restTemplate.exchange(basePathV1 + "/users/1/articles/1",
                GET, getHttpEntity(authResponseEntity), OpenArticleResponse.class).getBody();

        assertThat(responseAfter.getTitle()).isEqualTo(request.getTitle());
        assertThat(responseAfter.getBody()).isEqualTo(request.getBody());
        assertThat(responseAfter.getModificationDate()).isAfter(responseBefore.getModificationDate());
    }

    @Test
    void createArticleWithTagsAndSaveWithPublishedStatus() {
        String tag1 = "System test";
        String tag2 = "Spring Boot";
        String tag3 = "Java";

        ArticleRequest request = getArticleRequest()
                .tags(List.of(tag1, tag2, tag3))
                .build();
        HttpEntity<ArticleRequest> httpEntity = getHttpEntity(request, authResponseEntity);

        OpenArticleResponse newArticleResponse = restTemplate.exchange(basePathV1 + "/users/1/articles",
                POST, httpEntity, OpenArticleResponse.class).getBody();
        OpenArticleResponse getArticleResponse = restTemplate.exchange(basePathV1 + "/users/1/articles/"
                + newArticleResponse.getId(), GET, httpEntity, OpenArticleResponse.class).getBody();

        assertThat(getArticleResponse.getTitle()).isEqualTo(request.getTitle());
        assertThat(getArticleResponse.getStatus()).isEqualTo(StatusArticle.PUBLISHED.name());

        ParameterizedTypeReference<ResponsePage<TagResponse>> ptr = new ParameterizedTypeReference<>() {
        };
        ResponsePage<TagResponse> tagResponse = restTemplate.exchange(basePathV1 + "/tags", GET,
                httpEntity, ptr).getBody();

        assertThat(tagResponse.getContent().toString()).contains(tag1, tag2, tag3);
    }

    @Test
    void createArticleWithoutTagsAndSaveWithCreatedStatus() {
        ArticleRequest request = getArticleRequest()
                .status(StatusArticle.CREATED.name())
                .build();

        OpenArticleResponse newArticleResponse = restTemplate.exchange(basePathV1 + "/users/1/articles",
                POST, getHttpEntity(request, authResponseEntity), OpenArticleResponse.class).getBody();
        OpenArticleResponse getArticleResponse = restTemplate.exchange(basePathV1 + "/users/1/articles/"
                + newArticleResponse.getId(), GET, getHttpEntity(authResponseEntity), OpenArticleResponse.class).getBody();

        assertThat(getArticleResponse.getTitle()).isEqualTo(request.getTitle());
        assertThat(getArticleResponse.getStatus()).isEqualTo(StatusArticle.valueOf(request.getStatus()).toString());
    }
}
