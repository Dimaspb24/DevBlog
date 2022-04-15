package com.project.devblog.controller.mockcases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devblog.config.annotation.ITWithContextConfig;
import com.project.devblog.dto.request.UserRequest;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenUtil;
import com.project.devblog.service.UserService;
import com.project.devblog.testcontainers.PostgresITContainer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ITWithContextConfig
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends PostgresITContainer {

    static UserEntity user;
    final ObjectMapper mapper = new ObjectMapper();

    final UserService userService;
    final MockMvc mockMvc;
    final JwtTokenUtil jwtTokenUtil;

    @BeforeAll
    static void init() {
        final PersonalInfo personalInfo = new PersonalInfo("firstname", "lastname",
                "nickname", "photo", "user info", "891245646544");
        user = new UserEntity(UUID.randomUUID().toString(), "user@mail.ru", "encryptedPassword",
                Role.USER, true, null, personalInfo);
    }

    @Test
    void getUserTest() throws Exception {
        when(userService.findById(user.getId())).thenReturn(user);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenUtil.TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        final MvcResult mvcResult = mockMvc
                .perform(get("/v1/users/{userId}", user.getId()).header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        final UserEntity response = mapper
                .readValue(mvcResult.getResponse().getContentAsString(), UserEntity.class);
        assertThat(response).isEqualTo(user);
    }

    @Test
    void getNotExistUserTest() throws Exception {
        when(userService.findById(user.getId())).thenThrow(NotFoundException.class);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenUtil.TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        mockMvc
                .perform(get("/v1/users/{userId}", user.getId()).header("Authorization", token))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getAllUsersTest() throws Exception {
        final PersonalInfo personalInfo = new PersonalInfo("firstname", "lastname",
                "nickname", "photo", "user info", "891245646544");
        final UserEntity secondUser = new UserEntity(UUID.randomUUID().toString(), "user@mail.ru",
                "encryptedPassword", Role.USER, true, null, personalInfo);

        PageImpl<UserEntity> page = new PageImpl<>(List.of(user, secondUser));
        when(userService.findAll(Pageable.ofSize(2))).thenReturn(page);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenUtil.TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        final MvcResult mvcResult = mockMvc
                .perform(get("/v1/users")
                        .header("Authorization", token)
                        .queryParam("size", "2"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains(String.format("\"id\":\"%s\"", user.getId()));
        assertThat(mvcResult.getResponse().getContentAsString()).contains(String.format("\"id\":\"%s\"", secondUser.getId()));
    }

    @Test
    void blockUserTest() throws Exception {
        doNothing().when(userService).delete(user.getId());
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenUtil.TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        mockMvc
                .perform(delete("/v1/users/{userId}", user.getId())
                        .header("Authorization", token))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void enableUserTest() throws Exception {
        doNothing().when(userService).enable(user.getId(), true);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenUtil.TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        mockMvc
                .perform(patch("/v1/users/{userId}", user.getId())
                        .queryParam("enabled", "true")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void updateUserTest() throws Exception {
        final UserRequest request = new UserRequest("89123123123", "firstname", "lastname",
                "nickname", "info", "photo");
        user.setPersonalInfo(new PersonalInfo("firstname", "lastname", "nickname",
                "photo", "info", "89123123123"));

        when(userService.update(eq(user.getId()), any())).thenReturn(user);
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        final String token = JwtTokenUtil.TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        final MvcResult mvcResult = mockMvc
                .perform(put("/v1/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        final UserEntity response = mapper
                .readValue(mvcResult.getResponse().getContentAsString(), UserEntity.class);
        assertThat(response.getPersonalInfo()).isEqualTo(user.getPersonalInfo());
    }
}
