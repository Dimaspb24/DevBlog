package com.project.devblog;

import com.project.devblog.dto.request.UserRequest;
import com.project.devblog.exception.NonUniqueValueException;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.UserService;
import com.project.devblog.testcontainers.AbstractPostgresTestcontainer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest_IT extends AbstractPostgresTestcontainer {

    @Autowired
    UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mockMvc;
    final ObjectMapper mapper = new ObjectMapper();

    UserEntity user;
    UserEntity user2;
    String token;

    @BeforeEach
    void init() {
        final PersonalInfo personalInfo = PersonalInfo.builder()
                .nickname("nickname")
                .phone("88005553535")
                .firstname("Aleksey")
                .lastname("Alekseev")
                .info("info")
                .photo("https://ghjklvhhvhvh.ujhuh")
                .build();
        user = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .role(Role.USER)
                .login("login1@mail.ru")
                .password("pass1")
                .verificationCode(UUID.randomUUID().toString())
                .personalInfo(personalInfo)
                .build();
        userService.save(user);

        final PersonalInfo personalInfo2 = PersonalInfo.builder()
                .nickname("nickname2")
                .phone("88888888888")
                .firstname("Aleksey")
                .lastname("Alekseev")
                .info("info")
                .photo("https://ghjklvhhvhvh.ujhuh")
                .build();
        user2 = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .role(Role.USER)
                .login("login2@mail.ru")
                .password("pass2")
                .verificationCode(UUID.randomUUID().toString())
                .personalInfo(personalInfo2)
                .build();
        userService.save(user2);

        token = JwtTokenProvider.TOKEN_PREFIX + jwtTokenProvider.createToken(user.getLogin(), Role.USER);
    }

    @Test
    void findTest() throws Exception {
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
    void findTestNotExistUser() throws Exception {
        final String id = UUID.randomUUID().toString();
        mockMvc
                .perform(get("/v1/users/{userId}", id).header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                        .isEqualTo(format("%s with id=%s not found", UserEntity.class.getSimpleName(), id)));
    }

    @Test
    void updateUserTest() throws Exception {
        final UserRequest request = UserRequest.builder()
                .nickname("newNickname")
                .phone("88008005050")
                .firstname("Aleksey")
                .lastname("Alekseev")
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
        assertThat(response.getPersonalInfo().getInfo()).isEqualTo(updatedUser.getPersonalInfo().getInfo());
        assertThat(response.getPersonalInfo().getPhoto()).isEqualTo(updatedUser.getPersonalInfo().getPhoto());
    }

    @Test
    void updateUserTest2() throws Exception {
        final UserRequest request = UserRequest.builder()
                .nickname("nickname2")
                .phone("88008005050")
                .firstname("Aleksey")
                .lastname("Alekseev")
                .info("newInfo")
                .photo("https://ghjklvhhvhvh.ujhuh")
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
    void updateUserTest3() throws Exception {
        final UserRequest request = UserRequest.builder()
                .nickname(UUID.randomUUID().toString())
                .phone("88888888888")
                .firstname("Aleksey")
                .lastname("Alekseev")
                .info("newInfo")
                .photo("https://ghjklvhhvhvh.ujhuh")
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

    @AfterEach
    void clear() {
        userRepository.delete(user);
        userRepository.delete(user2);
    }
}
