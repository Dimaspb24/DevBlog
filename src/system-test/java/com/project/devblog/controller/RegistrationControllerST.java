package com.project.devblog.controller;

import com.project.devblog.config.annotation.ST;
import com.project.devblog.dto.request.CommentRequest;
import com.project.devblog.dto.request.RegistrationRequest;
import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.dto.response.CommentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

@ST
public class RegistrationControllerST extends BaseAuthController {

    @Test
    @DisplayName("Регистрация нового пользователя")
    void registrationTest() {
        var entity = new HttpEntity<>(RegistrationRequest.builder()
                .login("qwerty@mail.ru")
                .password("password")
                .build(), headers);
        var responseType = new ParameterizedTypeReference<>() {
        };
        var response = restTemplate.exchange(
                basePathV1 + "/auth/registration",
                POST, entity, responseType);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }
}
