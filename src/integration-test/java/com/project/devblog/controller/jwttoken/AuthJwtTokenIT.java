package com.project.devblog.controller.jwttoken;

import com.project.devblog.config.annotation.IT;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.testcontainers.PostgresITContainer;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.project.devblog.security.JwtTokenProvider.AUTH_HEADER_KEY;
import static com.project.devblog.security.JwtTokenProvider.TOKEN_PREFIX;
import static com.project.devblog.testdata.CommonData.AUTHOR_ID;
import static com.project.devblog.testdata.CommonData.AUTHOR_LOGIN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AutoConfigureMockMvc
@RequiredArgsConstructor
class AuthJwtTokenIT extends PostgresITContainer {

    MockMvc mockMvc;
    JwtTokenProvider jwtTokenProvider;

    @NonNull
    private String getValidToken() {
        return TOKEN_PREFIX + jwtTokenProvider.createToken(AUTHOR_LOGIN, Role.USER);
    }

    @Test
    void update_shouldReturnUnauthorized_whenWithoutAuthorizationHeader() throws Exception {
        mockMvc.perform(
                get("/v1/users/{userId}", AUTHOR_ID)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.debugMessage").value("Full authentication is required to access this resource"));
    }

    @Test
    void update_shouldReturnUnauthorized_whenAuthorizationHeaderEmptyValue() throws Exception {
        mockMvc.perform(
                get("/v1/users/{userId}", AUTHOR_ID)
                        .header(AUTH_HEADER_KEY, "")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.debugMessage").value("Full authentication is required to access this resource"));
    }

    @Test
    void update_shouldReturnUnauthorized_whenExpiredAuthorizationToken() throws Exception {
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkaW1hQG1haWwucnUiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjQyMzM3MjI4LCJleHAiOjE2NDIzNDA4Mjh9.hErzIQ_fLlLFsTOyvfj_BD1vywrsdppyXOHZ-l8ZhEE";
        mockMvc.perform(
                get("/v1/users/{userId}", AUTHOR_ID)
                        .header(AUTH_HEADER_KEY, expiredToken)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                /*FIXME: should another message with expired token*/
                .andExpect(jsonPath("$.debugMessage").value("Full authentication is required to access this resource"));
    }

    @Test
    void update_shouldReturnUnauthorized_whenInvalidAuthorizationToken() throws Exception {
        mockMvc.perform(
                get("/v1/users/{userId}", AUTHOR_ID)
                        .header(AUTH_HEADER_KEY, getValidToken() + "1")
                        .contentType(APPLICATION_JSON))
                /*FIXME: should another message with wrong token*/
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_shouldReturnUnauthorized_whenValidAuthorizationToken() throws Exception {
        mockMvc.perform(
                get("/v1/users/{userId}", AUTHOR_ID)
                        .header(AUTH_HEADER_KEY, getValidToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
