package com.project.devblog;

import com.project.devblog.dto.request.AuthenticationRequest;
import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.model.UserEntity;
import com.project.devblog.service.AuthenticationService;
import com.project.devblog.service.UserService;
import com.project.devblog.service.VerificationService;
import com.project.devblog.testcontainers.AbstractPostgresTestcontainer;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest_Login_IT extends AbstractPostgresTestcontainer {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationService verificationService;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void loginTest() throws Exception {
        final String login = UUID.randomUUID() + "@mail.ru";
        final String password = "userpass";
        final AuthenticationResponse authenticationResponse = authenticationService.register(login, password);
        final UserEntity user = userService.findByLogin(login);
        verificationService.verify(user.getId(), user.getVerificationCode());
        final AuthenticationRequest request = new AuthenticationRequest(login, password);

        final MvcResult mvcResult = mockMvc
                .perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION))
                .andReturn();

        final String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).contains(String.format("\"id\":\"%s\"", authenticationResponse.getId()));
        assertThat(response).contains(String.format("\"login\":\"%s\"", authenticationResponse.getLogin()));
        assertThat(response).contains(String.format("\"role\":\"%s\"", authenticationResponse.getRole()));
    }

    @Test
    void loginUserNotFoundTest() throws Exception {
        final String login = UUID.randomUUID() + "@mail.ru";
        final String password = "userpass";
        final AuthenticationRequest request = new AuthenticationRequest(login, password);
        mockMvc
                .perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void loginUserNotVerifyEmail() throws Exception {
        final String login = UUID.randomUUID() + "@mail.ru";
        final String password = "userpass";
        authenticationService.register(login, password);
        final AuthenticationRequest request = new AuthenticationRequest(login, password);
        mockMvc
                .perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
