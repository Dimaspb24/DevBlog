package com.project.devblog.controller;

import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.utils.ResponsePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

class SortingArticlesST extends BaseST {

    @Test
    @DisplayName("Sorting articles by publication date and(or) rating")
    void sortArticlesByPublicationDateAndOrRating() {
        ResponsePage<CloseArticleResponse> response;
        HttpEntity<Void> entity = getHttpEntity(authResponseEntity);
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
            // FIXME
            if (contentByRating.get(i).getRating() != null && contentByRating.get(i + 1).getRating() != null) {
                assertThat(contentByRating.get(i).getRating())
                        .isGreaterThanOrEqualTo(contentByRating.get(i + 1).getRating());
            }
        }


        response = restTemplate.exchange(basePathV1 + "/articles?sort=rating,desc&sort=publicationDate,desc", GET,
                entity, ptr).getBody();

        assertThat(response).isNotNull();
        List<CloseArticleResponse> contentByRatingAndPublicationDate = response.getContent();
        for (int i = 0; i < contentByRatingAndPublicationDate.size() - 1; i++) {
            Double ratingFirst = contentByRatingAndPublicationDate.get(i).getRating();
            Double ratingSecond = contentByRatingAndPublicationDate.get(i + 1).getRating();
            // FIXME
            if (ratingFirst != null && ratingSecond != null) {
                assertThat(ratingFirst).isGreaterThanOrEqualTo(ratingSecond);
            }

            if (Objects.equals(ratingFirst, ratingSecond)) {
                assertThat(contentByRatingAndPublicationDate.get(i).getPublicationDate())
                        .isAfterOrEqualTo(contentByRatingAndPublicationDate.get(i + 1).getPublicationDate());
            }
        }
    }
}
