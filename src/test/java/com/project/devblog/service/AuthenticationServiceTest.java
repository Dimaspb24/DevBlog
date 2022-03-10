package com.project.devblog.service;

import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void loginTest() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String login = "user1@gmail.com";
        final String password = "pass1";
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse(id,
                login, Role.USER.name());

        Mockito.when(userService.findByLogin(login))
                .thenReturn(UserEntity.builder()
                        .id(id)
                        .login(login)
                        .password(password)
                        .role(Role.USER)
                        .enabled(true)
                        .verificationCode(null)
                        .build());

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);

        ResponseEntity<AuthenticationResponse> response = authenticationService.login(login, password);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)).isTrue();
        assertThat(response.getHeaders().containsKey(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)).isTrue();
        assertThat(jwtTokenProvider.validateToken(response.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0))).isTrue();
        assertThat(response.getBody().getId()).isEqualTo(authenticationResponse.getId());
        assertThat(response.getBody().getLogin()).isEqualTo(authenticationResponse.getLogin());
        assertThat(response.getBody().getRole()).isEqualTo(authenticationResponse.getRole());
    }

    @Test
    void loginTestWithNotFoundUser() throws Exception {
        final String login = "user1@gmail.com";
        final String password = "pass1";

        Mockito.when(userService.findByLogin(login))
                .thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> {
            authenticationService.login(login, password);
        })
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("The account with login %s does not exist", login));
    }

    @Test
    void loginTestWithBlockedUser() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String login = "user1@gmail.com";
        final String password = "pass1";

        Mockito.when(userService.findByLogin(login))
                .thenReturn(UserEntity.builder()
                        .id(id)
                        .login(login)
                        .password(password)
                        .role(Role.USER)
                        .enabled(false)
                        .verificationCode(null)
                        .build());

        assertThatThrownBy(() -> {
            authenticationService.login(login, password);
        })
                .isInstanceOf(LockedException.class)
                .hasMessageContaining("This account is blocked");
    }

    @Test
    void loginTestWithNotVerifiedUser() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String login = "user1@gmail.com";
        final String password = "pass1";
        final String verificationCode = UUID.randomUUID().toString();

        Mockito.when(userService.findByLogin(login))
                .thenReturn(UserEntity.builder()
                        .id(id)
                        .login(login)
                        .password(password)
                        .role(Role.USER)
                        .enabled(false)
                        .verificationCode(verificationCode)
                        .build());

        assertThatThrownBy(() -> {
            authenticationService.login(login, password);
        })
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("This account is not verified");
    }

    @Test
    void registerTest() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String login = "user1@gmail.com";
        final String password = "pass1";
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse(id,
                login, Role.USER.name());

        Mockito.when(userService.isExists(login))
                        .thenReturn(false);
        Mockito.when(userService.register(login, password))
                .thenReturn(UserEntity.builder()
                        .id(id)
                        .login(login)
                        .password(password)
                        .role(Role.USER)
                        .build());

        ResponseEntity<AuthenticationResponse> response = authenticationService.register(login, password);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isEqualTo(authenticationResponse.getId());
        assertThat(response.getBody().getLogin()).isEqualTo(authenticationResponse.getLogin());
        assertThat(response.getBody().getRole()).isEqualTo(authenticationResponse.getRole());
    }
}
