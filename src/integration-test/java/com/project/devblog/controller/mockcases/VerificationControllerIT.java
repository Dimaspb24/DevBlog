package com.project.devblog.controller.mockcases;

import com.project.devblog.config.annotation.ITWithContextConfig;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenUtil;
import com.project.devblog.service.UserService;
import com.project.devblog.service.VerificationService;
import com.project.devblog.testcontainers.PostgresITContainer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.project.devblog.security.JwtTokenUtil.TOKEN_PREFIX;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ITWithContextConfig
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AutoConfigureMockMvc
@RequiredArgsConstructor
class VerificationControllerIT extends PostgresITContainer {

    final UserService userService;
    final VerificationService verificationService;
    final MockMvc mockMvc;
    final JwtTokenUtil jwtTokenUtil;

    @Test
    void confirmVerificationCodeTest() throws Exception {
        final PersonalInfo personalInfo = new PersonalInfo("firstname", "lastname",
                "nickname", "photo", "user info", "891245646544");
        final UserEntity user = new UserEntity(UUID.randomUUID().toString(), "user@mail.ru", "encryptedPassword",
                Role.USER, true, null, personalInfo);

        final String token = TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        final String verificationCode = UUID.randomUUID().toString();

        when(userService.findByLogin(user.getLogin())).thenReturn(user);
        doNothing().when(verificationService).verify(user.getId(), verificationCode);

        mockMvc
                .perform(get("/v1/users/{userId}/verify", user.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .queryParam("code", verificationCode))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void confirmNotValidVerificationCodeTest() throws Exception {
        final PersonalInfo personalInfo = new PersonalInfo("firstname", "lastname",
                "nickname", "photo", "user info", "891245646544");
        final UserEntity user = new UserEntity(UUID.randomUUID().toString(), "user@mail.ru", "encryptedPassword",
                Role.USER, true, null, personalInfo);

        final String token = TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        final String verificationCode = UUID.randomUUID().toString();

        when(userService.findByLogin(user.getLogin())).thenReturn(user);
        doThrow(VerificationException.class).when(verificationService).verify(user.getId(), verificationCode);

        mockMvc
                .perform(get("/v1/users/{userId}/verify", user.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .queryParam("code", verificationCode))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void confirmAlreadyVerifiedCodeTest() throws Exception {
        final PersonalInfo personalInfo = new PersonalInfo("firstname", "lastname",
                "nickname", "photo", "user info", "891245646544");
        final UserEntity user = new UserEntity(UUID.randomUUID().toString(), "user@mail.ru", "encryptedPassword",
                Role.USER, true, null, personalInfo);

        final String token = TOKEN_PREFIX + jwtTokenUtil.createToken(user.getLogin(), Role.USER);
        final String verificationCode = UUID.randomUUID().toString();

        when(userService.findByLogin(user.getLogin())).thenReturn(user);
        doThrow(VerificationException.class).when(verificationService).verify(user.getId(), verificationCode);

        mockMvc
                .perform(get("/v1/users/{userId}/verify", user.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .queryParam("code", verificationCode))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
