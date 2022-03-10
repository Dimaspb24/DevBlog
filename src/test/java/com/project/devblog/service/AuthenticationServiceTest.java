package com.project.devblog.service;

import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private static UserEntity user;
    private static AuthenticationResponse authenticationResponse;

    @BeforeAll
    static void init() {
        user = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .login("user1@gmail.com")
                .password("pass1")
                .role(Role.USER)
                .build();
        authenticationResponse = new AuthenticationResponse(user.getId(), user.getLogin(), Role.USER.name());
    }

    @Test
    void loginTest() throws Exception {
        user.setEnabled(true);
        user.setVerificationCode(null);

        when(userService.findByLogin(user.getLogin()))
                .thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        ResponseEntity<AuthenticationResponse> response = authenticationService.login(user.getLogin(),
                user.getPassword());

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
        when(userService.findByLogin(user.getLogin()))
                .thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> authenticationService.login(user.getLogin(), user.getPassword()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("The account with login %s does not exist", user.getLogin()));
    }

    @Test
    void loginTestWithBlockedUser() throws Exception {
        user.setEnabled(false);
        user.setVerificationCode(null);

        when(userService.findByLogin(user.getLogin()))
                .thenReturn(user);

        assertThatThrownBy(() -> authenticationService.login(user.getLogin(), user.getPassword()))
                .isInstanceOf(LockedException.class)
                .hasMessageContaining("This account is blocked");
    }

    @Test
    void loginTestWithNotVerifiedUser() throws Exception {
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());

        when(userService.findByLogin(user.getLogin()))
                .thenReturn(user);

        assertThatThrownBy(() -> authenticationService.login(user.getLogin(), user.getPassword()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("This account is not verified");
    }

    @Test
    void registerTest() throws Exception {
        when(userService.isExists(user.getLogin()))
                .thenReturn(false);
        when(userService.register(user.getLogin(), user.getPassword()))
                .thenReturn(user);

        ResponseEntity<AuthenticationResponse> response = authenticationService.register(user.getLogin(),
                user.getPassword());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isEqualTo(authenticationResponse.getId());
        assertThat(response.getBody().getLogin()).isEqualTo(authenticationResponse.getLogin());
        assertThat(response.getBody().getRole()).isEqualTo(authenticationResponse.getRole());
    }

    @Test
    void registerTestWithBadCredentials() throws Exception {
        when(userService.isExists(user.getLogin()))
                .thenReturn(true);

        assertThatThrownBy(() -> authenticationService.register(user.getLogin(), user.getPassword()))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining(format("The account with login %s is already registered", user.getLogin()));
    }
}
