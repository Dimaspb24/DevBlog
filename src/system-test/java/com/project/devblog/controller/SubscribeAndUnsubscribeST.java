package com.project.devblog.controller;

import com.project.devblog.dto.response.SubscriptionResponse;
import com.project.devblog.utils.ResponsePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

class SubscribeAndUnsubscribeST extends BaseST {

    @Test
    @DisplayName("Checking the user's ability to subscribe and unsubscribe from the author")
    void subscribingAndUnsubscribingTest() {
        var entity = getHttpEntity(authResponseEntity);
        var responseType = new ParameterizedTypeReference<>() {
        };
        var response = restTemplate.exchange(basePathV1 + "/users/2/subscriptions/3",
                POST, entity, responseType);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);

        var responseType2 = new ParameterizedTypeReference<ResponsePage<SubscriptionResponse>>() {
        };
        var response2 = restTemplate.exchange(basePathV1 + "/users/2/subscriptions",
                HttpMethod.GET, entity, responseType2);
        assertThat(response2).isNotNull();
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().getContent()).isNotNull();
        assertTrue(response2.getBody().getContent().stream().anyMatch(sub -> sub.getAuthorId().equals("3")));

        restTemplate.exchange(basePathV1 + "/users/2/subscriptions/3",
                DELETE, entity, responseType);

        response2 = restTemplate.exchange(basePathV1 + "/users/2/subscriptions",
                HttpMethod.GET, entity, responseType2);
        assertThat(response2).isNotNull();
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().getContent()).isNotNull();
        assertFalse(response2.getBody().getContent().stream().anyMatch(sub -> sub.getAuthorId().equals("3")));
    }
}
