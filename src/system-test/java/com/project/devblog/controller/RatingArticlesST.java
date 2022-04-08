package com.project.devblog.controller;

import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.RatingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.project.devblog.testdata.TestData.getArticleRequest;
import static com.project.devblog.testdata.TestData.getRatingRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

class RatingArticlesST extends BaseST {

    /**
     * создание статьи юзером 1
     * оценка статьи (4) юзером 2
     * проверка рейтинга (4) статьи юзером 1
     * оценка статьи (6) юзером 3
     * проверка рейтинга статьи (5) юзером 1
     * обновление рейтинга (8)  статьи юзером 2
     * проверка рейтинга статьи (7) юзером 1
     */
    @Test
    @DisplayName("Checking the rating of an article when rated by two users")
    void rateArticles() {
        ResponseEntity<AuthenticationResponse> authResponseUser1 = signIn(USER1_LOGIN, PASSWORD);
        String idUser1 = Objects.requireNonNull(authResponseUser1.getBody()).getId();
        ResponseEntity<AuthenticationResponse> authResponseUser2 = signIn(USER2_LOGIN, PASSWORD);
        String idUser2 = Objects.requireNonNull(authResponseUser2.getBody()).getId();
        ResponseEntity<AuthenticationResponse> authResponseUser3 = signIn(USER3_LOGIN, PASSWORD);
        String idUser3 = Objects.requireNonNull(authResponseUser3.getBody()).getId();


        // create Article by User 1
        ResponseEntity<OpenArticleResponse> responseOpenArticleForUser1 = restTemplate.exchange(
                String.format("%s/users/%s/articles", basePathV1, idUser1), POST,
                getHttpEntity(getArticleRequest().build(), authResponseUser1),
                ParameterizedTypeReference.forType(OpenArticleResponse.class));

        assertThat(responseOpenArticleForUser1.getBody()).isNotNull();


        // rate article by User 2
        Integer articleId = Objects.requireNonNull(responseOpenArticleForUser1.getBody()).getId();

        ResponseEntity<RatingResponse> responseRatingForUser2 = restTemplate.exchange(
                String.format("%s/users/%s/articles/%d/ratings", basePathV1, idUser2, articleId), POST,
                getHttpEntity(getRatingRequest().rating(4).build(), authResponseUser2),
                ParameterizedTypeReference.forType(RatingResponse.class));

        assertThat(responseRatingForUser2.getBody()).isNotNull();


        // check common rating article by User 1
        ResponseEntity<OpenArticleResponse> responseCheckingOpenArticleForUser1First = restTemplate.exchange(
                String.format("%s/articles/%d", basePathV1, articleId), GET,
                getHttpEntity(authResponseUser1),
                ParameterizedTypeReference.forType(OpenArticleResponse.class));

        assertThat(responseCheckingOpenArticleForUser1First)
                .isNotNull()
                .extracting(responseEntity -> Objects.requireNonNull(responseEntity.getBody()).getRating())
                .isEqualTo(4.0);


        // rate article by User 3
        ResponseEntity<RatingResponse> responseRatingForUser3 = restTemplate.exchange(
                String.format("%s/users/%s/articles/%d/ratings", basePathV1, idUser3, articleId), POST,
                getHttpEntity(getRatingRequest().rating(6).build(), authResponseUser3),
                ParameterizedTypeReference.forType(RatingResponse.class));

        assertThat(responseRatingForUser3.getBody()).isNotNull();


        // check common rating article equals 5 (by User 1)
        ResponseEntity<OpenArticleResponse> responseCheckingOpenArticleForUser1Second = restTemplate.exchange(
                String.format("%s/articles/%d", basePathV1, articleId), GET,
                getHttpEntity(authResponseUser1),
                ParameterizedTypeReference.forType(OpenArticleResponse.class));

        assertThat(responseCheckingOpenArticleForUser1Second)
                .isNotNull()
                .extracting(responseEntity -> Objects.requireNonNull(responseEntity.getBody()).getRating())
                .isEqualTo(5.0);


        // change rating article on 8 (by User 2)
        ResponseEntity<RatingResponse> responseUpdatedRatingForUser2 = restTemplate.exchange(
                String.format("%s/users/%s/articles/%d/ratings", basePathV1, idUser2, articleId), POST,
                getHttpEntity(getRatingRequest().rating(8).build(), authResponseUser2),
                ParameterizedTypeReference.forType(RatingResponse.class));

        assertThat(responseUpdatedRatingForUser2.getBody()).isNotNull();


        // check common rating article equals 7 (by User 1)
        ResponseEntity<OpenArticleResponse> responseCheckingOpenArticleForUser1Third = restTemplate.exchange(
                String.format("%s/articles/%d", basePathV1, articleId), GET,
                getHttpEntity(authResponseUser1),
                ParameterizedTypeReference.forType(OpenArticleResponse.class));

        assertThat(responseCheckingOpenArticleForUser1Third)
                .isNotNull()
                .extracting(responseEntity -> Objects.requireNonNull(responseEntity.getBody()).getRating())
                .isEqualTo(7.0);
    }
}
