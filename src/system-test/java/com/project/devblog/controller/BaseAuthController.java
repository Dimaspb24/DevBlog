package com.project.devblog.controller;

import com.project.devblog.dto.request.AuthenticationRequest;
import com.project.devblog.dto.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class BaseAuthController {

    protected final HttpHeaders headers = new HttpHeaders();
    protected final TestRestTemplate restTemplate = new TestRestTemplate();
    protected String basePathV1;

    private final AuthenticationRequest authRequest = new AuthenticationRequest("mail1@mail.ru", "password");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void signIn() {
        basePathV1 = "http://localhost:" + port + "/v1";
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                basePathV1 + "/auth/login", authRequest, AuthenticationResponse.class);
        headers.set(AUTHORIZATION, Objects.requireNonNull(response.getHeaders().get(AUTHORIZATION)).get(0));
    }
}
