package com.project.devblog.integration.controller;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.integration.config.annotation.IT;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.ArticleService;
import com.project.devblog.service.SubscriptionService;
import com.project.devblog.service.UserService;
import com.project.devblog.testcontainers.PostgresTestContainer;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.project.devblog.integration.CommonData.AUTHOR_LOGIN;
import static com.project.devblog.security.JwtTokenProvider.TOKEN_PREFIX;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserSubscriptionControllerTest extends PostgresTestContainer {

    ArticleService articleService;
    SubscriptionService subscriptionService;
    UserService userService;
    JwtTokenProvider jwtTokenProvider;
    MockMvc mockMvc;

    @NonFinal
    UserEntity user;

    @BeforeEach
    void init() {
        user = userService.findById("1");
    }

    @Test
    void Subscribe_ExistsAuthor_Success() throws Exception {
        final String authorId = "2";

        List<UserEntity> subscriptions = subscriptionService.findSubscriptions(user.getId(), Pageable.unpaged())
                .getContent();
        assertThat(subscriptions.stream().anyMatch(sub -> sub.getId().equals(authorId))).isFalse();

        mockMvc
                .perform(post("/v1/users/{userId}/subscriptions/{authorId}", user.getId(), authorId)
                        .header("Authorization", getValidToken()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        subscriptions = subscriptionService.findSubscriptions(user.getId(), Pageable.unpaged()).getContent();
        assertThat(subscriptions.stream().anyMatch(sub -> sub.getId().equals(authorId))).isTrue();
    }

    @Test
    void Subscribe_NotExistsAuthor_NotFoundException() throws Exception {
        final String authorId = UUID.randomUUID().toString();

        mockMvc
                .perform(post("/v1/users/{userId}/subscriptions/{authorId}", user.getId(), authorId)
                        .header("Authorization", getValidToken()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .isEqualTo(format("%s with id=%s not found", UserEntity.class.getSimpleName(), authorId)));
    }

    @Test
    void Unsubscribe_Success() throws Exception {
        final String authorId = "6";

        List<UserEntity> subscriptions = subscriptionService.findSubscriptions(user.getId(), Pageable.unpaged())
                .getContent();
        assertThat(subscriptions.stream().anyMatch(sub -> sub.getId().equals(authorId))).isTrue();

        mockMvc
                .perform(delete("/v1/users/{userId}/subscriptions/{authorId}", user.getId(), authorId)
                        .header("Authorization", getValidToken()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        subscriptions = subscriptionService.findSubscriptions(user.getId(), Pageable.unpaged()).getContent();
        assertThat(subscriptions.stream().anyMatch(sub -> sub.getId().equals(authorId))).isFalse();
    }

    @Test
    void Unsubscribe_NotExistsAuthor_NotFoundException() throws Exception {
        final String authorId = UUID.randomUUID().toString();

        mockMvc
                .perform(delete("/v1/users/{userId}/subscriptions/{authorId}", user.getId(), authorId)
                        .header("Authorization", getValidToken()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .isEqualTo(format("%s with id=%s not found", UserEntity.class.getSimpleName(), authorId)));
    }

    @Test
    void FindArticlesBySubscriptions_Success() throws Exception {
        final ArticleEntity article = articleService.findById(12);
        final int articlesCount = articleService.findBySubscriptions(user.getId(), Pageable.unpaged()).getContent().size();

        final MvcResult mvcResult = mockMvc
                .perform(get("/v1/users/{userId}/subscriptions/articles", user.getId())
                        .header("Authorization", getValidToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final String response = mvcResult.getResponse().getContentAsString();

        assertThat(response)
                .contains(String.format("\"totalElements\":%s", articlesCount))
                .contains(String.format("\"id\":%s", article.getId()))
                .contains(String.format("\"title\":\"%s\"", article.getTitle()));
    }

    @NonNull
    private String getValidToken() {
        return TOKEN_PREFIX + jwtTokenProvider.createToken(AUTHOR_LOGIN, Role.USER);
    }
}
