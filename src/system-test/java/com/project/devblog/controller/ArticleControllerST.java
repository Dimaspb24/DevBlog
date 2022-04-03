package com.project.devblog.controller;

import com.project.devblog.config.annotation.ST;
import com.project.devblog.dto.request.AuthenticationRequest;
import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.testcontainers.PostgresSTContainer;
import com.project.devblog.utils.ResponsePage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

@ST
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class ArticleControllerST extends PostgresSTContainer {

    final ArticleController articleController;

    final HttpHeaders headers = new HttpHeaders();
    final TestRestTemplate restTemplate = new TestRestTemplate();
    final AuthenticationRequest authRequest = new AuthenticationRequest("mail1@mail.ru", "password");

    @LocalServerPort
    Integer port;

    String basePathV1;

    @BeforeEach
    void signIn() {
        basePathV1 = "http://localhost:" + port + "/v1";
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                basePathV1 + "/auth/login", authRequest, AuthenticationResponse.class);
        headers.set(AUTHORIZATION, Objects.requireNonNull(response.getHeaders().get(AUTHORIZATION)).get(0));
    }

    @Test
    void sortArticlesByPublicationDateAndOrRating() {
        ResponsePage<CloseArticleResponse> response;
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);
        ParameterizedTypeReference<ResponsePage<CloseArticleResponse>> ptr = new ParameterizedTypeReference<>() {
        };


        response = restTemplate.exchange(basePathV1 + "/articles", GET, entity, ptr).getBody();

        assertThat(response).isNotNull();
        List<CloseArticleResponse> contentByPublicationDate = response.getContent();
        for (int i = 0; i < contentByPublicationDate.size() - 1; i++) {
            assertThat(contentByPublicationDate.get(i).getPublicationDate())
                    .isBeforeOrEqualTo(contentByPublicationDate.get(i + 1).getPublicationDate());
        }


        response = restTemplate.exchange(basePathV1 + "/articles?sort=rating,desc", GET, entity, ptr).getBody();

        assertThat(response).isNotNull();
        List<CloseArticleResponse> contentByRating = response.getContent();
        for (int i = 0; i < contentByRating.size() - 1; i++) {
            assertThat(contentByRating.get(i).getRating())
                    .isGreaterThanOrEqualTo(contentByRating.get(i + 1).getRating());
        }


        response = restTemplate.exchange(basePathV1 + "/articles?sort=rating,desc&sort=publicationDate,desc", GET, entity, ptr).getBody();

        assertThat(response).isNotNull();
        List<CloseArticleResponse> contentByRatingAndPublicationDate = response.getContent();
        for (int i = 0; i < contentByRatingAndPublicationDate.size() - 1; i++) {
            assertThat(contentByRatingAndPublicationDate.get(i).getRating())
                    .isGreaterThanOrEqualTo(contentByRatingAndPublicationDate.get(i + 1).getRating());

            Double ratingFirst = contentByRatingAndPublicationDate.get(i).getRating();
            Double ratingSecond = contentByRatingAndPublicationDate.get(i + 1).getRating();
            if (Objects.equals(ratingFirst, ratingSecond)) {
                assertThat(contentByRatingAndPublicationDate.get(i).getPublicationDate())
                        .isAfterOrEqualTo(contentByRatingAndPublicationDate.get(i + 1).getPublicationDate());
            }
        }
    }
}
