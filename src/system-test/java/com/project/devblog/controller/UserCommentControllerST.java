package com.project.devblog.controller;

import com.project.devblog.config.annotation.ST;
import com.project.devblog.dto.request.BookmarkRequest;
import com.project.devblog.dto.request.CommentRequest;
import com.project.devblog.dto.response.BookmarkResponse;
import com.project.devblog.dto.response.CommentResponse;
import com.project.devblog.utils.ResponsePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@ST
public class UserCommentControllerST extends BaseAuthController {

    @Test
    @DisplayName("Добавление комментария к существующей статье")
    void addingCommentTest() {
        var entity = new HttpEntity<>(CommentRequest.builder()
                .message("qqqqq")
                .build(), headers);
        var responseType = new ParameterizedTypeReference<CommentResponse>() {
        };
        var response = restTemplate.exchange(
                basePathV1 + "/users/2/articles/2/comments",
                POST, entity, responseType);
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("qqqqq");

        var entity2 = new HttpEntity<>(null, headers);
        var responseType2 = new ParameterizedTypeReference<ResponsePage<CommentResponse>>() {
        };
        var response2 = restTemplate.exchange(
                basePathV1 + "/users/2/articles/2/comments",
                GET, entity2, responseType2);
        assertThat(response2).isNotNull();
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().getContent()).isNotNull();
        assertThat(response2.getBody().getContent().size()).isEqualTo(1);
        assertThat(response2.getBody().getContent().stream().findFirst()).isNotNull();
        assertThat(response2.getBody().getContent().stream().findFirst().get().getMessage()).isEqualTo("qqqqq");
    }
}
