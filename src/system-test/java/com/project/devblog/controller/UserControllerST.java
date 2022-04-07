package com.project.devblog.controller;

import com.project.devblog.config.annotation.ST;
import com.project.devblog.dto.request.UserRequest;
import com.project.devblog.dto.response.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

@ST
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserControllerST extends BaseAuthController {

    @Test
    void updatePersonalInfo() {
        UserRequest request = UserRequest.builder()
                .phone("81112223344")
                .nickname("Челик")
                .build();

        HttpEntity<?> httpEntity = new HttpEntity<>(request, headers);
        restTemplate.exchange(basePathV1 + "/users/1", PUT, httpEntity, UserResponse.class);
        UserResponse response = restTemplate.exchange(basePathV1 + "/users/1", GET, httpEntity, UserResponse.class)
                .getBody();

        assertThat(response.getPersonalInfo().getPhone()).isEqualTo(request.getPhone());
        assertThat(response.getPersonalInfo().getNickname()).isEqualTo(request.getNickname());
    }
}
