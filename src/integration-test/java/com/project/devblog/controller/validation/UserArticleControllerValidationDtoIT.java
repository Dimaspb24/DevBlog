package com.project.devblog.controller.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devblog.config.annotation.IT;
import com.project.devblog.dto.request.ArticleRequest;
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

import java.util.List;

import static com.project.devblog.security.JwtTokenProvider.AUTH_HEADER_KEY;
import static com.project.devblog.security.JwtTokenProvider.TOKEN_PREFIX;
import static com.project.devblog.testdata.CommonData.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserArticleControllerValidationDtoIT extends PostgresITContainer {

    ObjectMapper mapper = new ObjectMapper();

    MockMvc mockMvc;
    JwtTokenProvider jwtTokenProvider;

    @NonNull
    private String getToken() {
        return TOKEN_PREFIX + jwtTokenProvider.createToken("mail1@mail.ru", Role.USER);
    }

    /*TODO: https://rieckpil.de/guide-to-testing-spring-boot-applications-with-mockmvc/
        https://github.com/rieckpil/blog-tutorials/tree/master/testing-spring-boot-applications-with-mockmvc/src/test/java/de/rieckpil/blog
        @WithMockUser("duke")
        user("duke").roles("ADMIN", "SUPER_USER")
        jwt(), oauth2Login()...
     */
    @Test
    void update_shouldReturnStatusOk_whenValidCountTags() throws Exception {
        ArticleRequest validNewArticleRequest = getNewArticleRequest().tags(List.of("1", "2", "3", "4")).build();

        mockMvc.perform(
                put("/v1/users/{userId}/articles/{articleId}", AUTHOR_ID, ARTICLE_ID)
                        .header(AUTH_HEADER_KEY, getToken())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(validNewArticleRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void update_shouldThrowException_whenInvalidCountTags() throws Exception {
        ArticleRequest invalidNewArticleRequest = getNewArticleRequest().tags(List.of("1", "2", "3", "4", "5")).build();

        mockMvc.perform(
                put("/v1/users/{userId}/articles/{articleId}", AUTHOR_ID, ARTICLE_ID)
                        .header(AUTH_HEADER_KEY, getToken())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(invalidNewArticleRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.apierror.message").value("Validation error"))
                .andExpect(jsonPath("$.apierror.subErrors[0].message").value("The maximum number of tags is 4"));
    }

    @Test
    void update_shouldReturnStatusOk_whenValidSizeTag() throws Exception {
        ArticleRequest validNewArticleRequest = getNewArticleRequest().tags(List.of("test1test2test3test4")).build();

        mockMvc.perform(
                put("/v1/users/{userId}/articles/{articleId}", AUTHOR_ID, ARTICLE_ID)
                        .header(AUTH_HEADER_KEY, getToken())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(validNewArticleRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void update_shouldThrowException_whenInvalidSizeTag() throws Exception {
        ArticleRequest invalidNewArticleRequest = getNewArticleRequest().tags(List.of("test1test2test3test4test5")).build();

        mockMvc.perform(
                put("/v1/users/{userId}/articles/{articleId}", AUTHOR_ID, ARTICLE_ID)
                        .header(AUTH_HEADER_KEY, getToken())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(invalidNewArticleRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.apierror.message").value("Validation error"))
                .andExpect(jsonPath("$.apierror.subErrors[0].message").value("The tag can contain up to 20 characters"));
    }
}
