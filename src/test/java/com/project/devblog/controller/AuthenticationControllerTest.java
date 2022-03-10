package com.project.devblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devblog.dto.request.AuthenticationRequest;
import com.project.devblog.dto.request.RegistrationRequest;
import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.AuthenticationService;
import com.project.devblog.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.project.devblog.security.JwtTokenProvider.TOKEN_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {

    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void registrationTest() throws Exception {
        final RegistrationRequest request = new RegistrationRequest("user@mail.ru", "userpass");
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse(UUID.randomUUID().toString(),
                request.getLogin(), Role.USER.name());
        Mockito.when(authenticationService.register(request.getLogin(), request.getPassword()))
                .thenReturn(new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED));

        final MvcResult mvcResult = mockMvc
                .perform(post("/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        final String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).contains(String.format("\"id\":\"%s\"", authenticationResponse.getId()));
        assertThat(response).contains(String.format("\"login\":\"%s\"", authenticationResponse.getLogin()));
        assertThat(response).contains(String.format("\"role\":\"%s\"", authenticationResponse.getRole()));
    }

    @Test
    void loginTest() throws Exception {
        final AuthenticationRequest request = new AuthenticationRequest("user@mail.ru", "userpass");
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse(UUID.randomUUID().toString(),
                request.getLogin(), Role.USER.name());
        final String token = jwtTokenProvider.createToken(request.getLogin(), Role.USER);
        Mockito.when(authenticationService.login(request.getLogin(), request.getPassword()))
                .thenReturn(ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
                        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
                        .body(authenticationResponse));

        final MvcResult mvcResult = mockMvc
                .perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION))
                .andReturn();

        final String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).contains(String.format("\"id\":\"%s\"", authenticationResponse.getId()));
        assertThat(response).contains(String.format("\"login\":\"%s\"", authenticationResponse.getLogin()));
        assertThat(response).contains(String.format("\"role\":\"%s\"", authenticationResponse.getRole()));
    }

    @Test
    void loginUserNotFoundTest() throws Exception {
        final AuthenticationRequest request = new AuthenticationRequest("user@mail.ru", "userpass");
        Mockito.when(authenticationService.login(request.getLogin(), request.getPassword()))
                .thenThrow(NotFoundException.class);

        mockMvc
                .perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void logoutTest() throws Exception {
        Mockito.doNothing().when(authenticationService).logout(any(), any());

        mockMvc
                .perform(post("/v1/auth/logout"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void checkTokenTest() throws Exception {
        final String token = TOKEN_PREFIX + jwtTokenProvider.createToken("user@mail.ru", Role.USER);

        final MvcResult mvcResult = mockMvc
                .perform(get("/v1/auth/checkToken")
                        .queryParam("token", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Token is valid");
    }

    @Test
    void checkTokenWithoutBearerPrefixTest() throws Exception {
        final String token = jwtTokenProvider.createToken("user@mail.ru", Role.USER);

        mockMvc
                .perform(get("/v1/auth/checkToken")
                        .queryParam("token", token))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void checkNotValidToken() throws Exception {
        final String token = TOKEN_PREFIX + UUID.randomUUID();
        mockMvc
                .perform(get("/v1/auth/checkToken")
                        .queryParam("token", token))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
