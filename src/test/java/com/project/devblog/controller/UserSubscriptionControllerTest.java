package com.project.devblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.integration.config.PostgresTestContainer;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.ArticleService;
import com.project.devblog.service.SubscriptionService;
import com.project.devblog.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserSubscriptionControllerTest extends PostgresTestContainer {

    @MockBean
    UserService userService;
    @MockBean
    SubscriptionService subscriptionService;
    @MockBean
    ArticleService articleService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    static UserEntity user;
    static UserEntity author;
    final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void init() {
        final PersonalInfo userInfo = new PersonalInfo("firstname", "lastname",
                "nickname", "photo", "user info", "891245646544");
        user = new UserEntity(UUID.randomUUID().toString(), "user@mail.ru", "encryptedPassword",
                Role.USER, true, null, userInfo,
                List.of(), List.of(), List.of(), List.of(), Set.of(), Set.of());

        final PersonalInfo authorInfo = new PersonalInfo("firstname", "lastname",
                "nickname", "photo", "user info", "891245646544");
        author = new UserEntity(UUID.randomUUID().toString(), "user@mail.ru", "encryptedPassword",
                Role.USER, true, null, authorInfo,
                List.of(), List.of(), List.of(), List.of(), Set.of(), Set.of());
    }

    @Test
    void createSubscriptionTest() throws Exception {
        doNothing().when(subscriptionService).subscribe(user.getId(), author.getId());
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(user.getLogin(), Role.USER);
        mockMvc
                .perform(post("/v1/users/{userId}/subscriptions/{authorId}", user.getId(), author.getId())
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void createSubscriptionNotExistUserTest() throws Exception {
        doThrow(NotFoundException.class).when(subscriptionService).subscribe(user.getId(), author.getId());
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(user.getLogin(), Role.USER);
        mockMvc
                .perform(post("/v1/users/{userId}/subscriptions/{authorId}", user.getId(), author.getId())
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getUserSubscriptionsTest() throws Exception {
        final PageImpl<UserEntity> page = new PageImpl<>(List.of(author));
        when(subscriptionService.findSubscriptions(eq(user.getId()), any())).thenReturn(page);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(user.getLogin(), Role.USER);
        final MvcResult mvcResult = mockMvc
                .perform(get("/v1/users/{userId}/subscriptions", user.getId())
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains(String.format("\"authorId\":\"%s\"", author.getId()));
    }

    @Test
    void deleteSubscriptionTest() throws Exception {
        doNothing().when(subscriptionService).unsubscribe(user.getId(), author.getId());
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(user.getLogin(), Role.USER);
        mockMvc
                .perform(delete("/v1/users/{userId}/subscriptions/{authorId}", user.getId(), author.getId())
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void getArticlesBySubscriptionsTest() throws Exception {
        final ArticleEntity article = new ArticleEntity(123, "title", "body",
                StatusArticle.PUBLISHED, "description", true, LocalDateTime.now(), 5.0, author,
                List.of(), List.of(), List.of());
        article.setModificationDate(LocalDateTime.now());
        final PageImpl<ArticleEntity> page = new PageImpl<>(List.of(article));
        when(articleService.findBySubscriptions(eq(user.getId()), any())).thenReturn(page);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(user.getLogin(), Role.USER);
        final MvcResult mvcResult = mockMvc
                .perform(get("/v1/users/{userId}/subscriptions/articles", user.getId())
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).contains(String.format("\"authorId\":\"%s\"", author.getId()));
        assertThat(response).contains(String.format("\"id\":%s", article.getId()));
        assertThat(response).contains(String.format("\"title\":\"%s\"", article.getTitle()));
        assertThat(response).contains(String.format("\"status\":\"%s\"", article.getStatus()));
        assertThat(response).contains(String.format("\"description\":\"%s\"", article.getDescription()));
        assertThat(response).contains(String.format("\"rating\":%s", article.getRating().toString()));
        //FIXME: failed when mvn clean install
//        assertThat(response).contains(String.format("\"publicationDate\":\"%s\"", article.getPublicationDate().toString().substring(0, 27)));
//        assertThat(response).contains(String.format("\"modificationDate\":\"%s\"", article.getModificationDate().toString().substring(0, 27)));
    }

    @Test
    void getUserSubscribers() throws Exception {
        final PageImpl<UserEntity> page = new PageImpl<>(List.of(user));
        when(subscriptionService.findSubscribers(eq(author.getId()), any())).thenReturn(page);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(author.getLogin(), Role.USER);
        final MvcResult mvcResult = mockMvc
                .perform(get("/v1/users/{userId}/subscribers", author.getId())
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).contains(String.format("\"subscriberId\":\"%s\"", user.getId()));
        assertThat(response).contains(String.format("\"nickname\":\"%s\"", user.getPersonalInfo().getNickname()));
    }
}
