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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc
public class UserSubscriptionControllerTest extends PostgresTestContainer {

    @Autowired
    ArticleService articleService;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    MockMvc mockMvc;

    UserEntity user;
    String token;

    @BeforeEach
    void init() {
        user = userService.findById("1");
        token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(user.getLogin(), Role.USER);
    }

    @Test
    void Subscribe_ExistsAuthor_Success() throws Exception {
        final String authorId = "2";

        List<UserEntity> subscriptions = subscriptionService.findSubscriptions(user.getId(), Pageable.unpaged())
                .getContent();
        assertThat(subscriptions.stream().anyMatch(sub -> sub.getId().equals(authorId))).isFalse();

        mockMvc
                .perform(post("/v1/users/{userId}/subscriptions/{authorId}", user.getId(), authorId)
                        .header("Authorization", token))
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
                        .header("Authorization", token))
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
                        .header("Authorization", token))
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
                        .header("Authorization", token))
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
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).contains(String.format("\"totalElements\":%s", articlesCount));
        assertThat(response).contains(String.format("\"id\":%s", article.getId()));
        assertThat(response).contains(String.format("\"title\":\"%s\"", article.getTitle()));
    }
}
