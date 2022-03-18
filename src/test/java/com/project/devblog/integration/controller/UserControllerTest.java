package com.project.devblog.integration.controller;

import com.project.devblog.dto.request.UserRequest;
import com.project.devblog.exception.NonUniqueValueException;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.integration.config.annotation.IT;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.UserService;
import com.project.devblog.testcontainers.PostgresTestContainer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc
public class UserControllerTest extends PostgresTestContainer {

    @Autowired
    UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    MockMvc mockMvc;
    final ObjectMapper mapper = new ObjectMapper();

    UserEntity user;
    String token;

    @BeforeEach
    void init() {
        user = userService.findById("1");
        token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(user.getLogin(), Role.USER);
    }

    @Test
    void Find_ExistsUser_Success() throws Exception {
        final MvcResult mvcResult = mockMvc
                .perform(get("/v1/users/{userId}", user.getId())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final UserEntity response = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserEntity.class);

        assertThat(response).isEqualTo(user);
    }

    @Test
    void Find_NotExistsUser_NotFoundException() throws Exception {
        final String idForFind = UUID.randomUUID().toString();

        mockMvc
                .perform(get("/v1/users/{userId}", idForFind).header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .isEqualTo(format("%s with id=%s not found", UserEntity.class.getSimpleName(), idForFind)));
    }

    @Test
    void Update_WithValidData_Success() throws Exception {
        final UserRequest request = UserRequest.builder()
                .nickname("newNickname")
                .phone("88008005050")
                .firstname("newName")
                .lastname("newName")
                .info("newInfo")
                .photo("https://ghjklvhhvhvh.ujhuh")
                .build();

        final MvcResult mvcResult = mockMvc
                .perform(put("/v1/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final UserEntity response = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserEntity.class);
        final UserEntity updatedUser = userService.findById(user.getId());

        assertThat(response.getPersonalInfo().getNickname()).isEqualTo(updatedUser.getPersonalInfo().getNickname());
        assertThat(response.getPersonalInfo().getPhone()).isEqualTo(updatedUser.getPersonalInfo().getPhone());
        assertThat(response.getPersonalInfo().getFirstname()).isEqualTo(updatedUser.getPersonalInfo().getFirstname());
        assertThat(response.getPersonalInfo().getLastname()).isEqualTo(updatedUser.getPersonalInfo().getLastname());
        assertThat(response.getPersonalInfo().getInfo()).isEqualTo(updatedUser.getPersonalInfo().getInfo());
        assertThat(response.getPersonalInfo().getPhoto()).isEqualTo(updatedUser.getPersonalInfo().getPhoto());
    }

    @Test
    void Update_WithAlreadyExistsNickName_NonUniqueValueException() throws Exception {
        final UserRequest request = UserRequest.builder()
                .nickname("Шрэк")
                .build();

        mockMvc
                .perform(put("/v1/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NonUniqueValueException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .isEqualTo(format("User with nickname=%s already exists", request.getNickname())));
    }

    @Test
    void Update_WithAlreadyExistsPhone_NonUniqueValueException() throws Exception {
        final UserRequest request = UserRequest.builder()
                .phone("85555555555")
                .build();

        mockMvc
                .perform(put("/v1/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NonUniqueValueException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .isEqualTo(format("User with this phone=%s already exists", request.getPhone())));
    }
}
