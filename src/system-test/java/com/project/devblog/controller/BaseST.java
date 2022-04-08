package com.project.devblog.controller;

import com.project.devblog.config.annotation.ST;
import com.project.devblog.dto.request.AuthenticationRequest;
import com.project.devblog.dto.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@ST
public class BaseST {

    protected static final TestRestTemplate restTemplate = new TestRestTemplate();
    protected static final String USER1_LOGIN = "mail1@mail.ru";
    protected static final String USER2_LOGIN = "mail2@mail.ru";
    protected static final String USER3_LOGIN = "mail3@mail.ru";
    protected static final String PASSWORD = "password";
    private static final String AUTH_LOGIN = "/auth/login";
    protected static ResponseEntity<AuthenticationResponse> authResponseEntity;
    protected static String basePathV1;
    @LocalServerPort
    private Integer port;

    public static ResponseEntity<AuthenticationResponse> signIn(String login, String password) {
        AuthenticationRequest authRequest = new AuthenticationRequest(login, password);
        return restTemplate.postForEntity(basePathV1 + AUTH_LOGIN, authRequest,
                AuthenticationResponse.class);
    }

    public static <T> HttpEntity<T> getHttpEntity(T t, ResponseEntity<AuthenticationResponse> responseEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, getAuthorizationTokenValue(responseEntity));
        return new HttpEntity<>(t, headers);
    }

    public static <T> HttpEntity<T> getHttpEntity(ResponseEntity<AuthenticationResponse> responseEntity) {
        return getHttpEntity(null, responseEntity);
    }

    private static String getAuthorizationTokenValue(ResponseEntity<AuthenticationResponse> responseEntity) {
        return Objects.requireNonNull(responseEntity.getHeaders().get(AUTHORIZATION)).get(0);
    }

    @PostConstruct
    void init() {
        basePathV1 = "http://localhost:" + port + "/v1";
    }

    @BeforeEach
    void signInWithTestUser() {
        authResponseEntity = signIn(USER1_LOGIN, PASSWORD);
    }
}
