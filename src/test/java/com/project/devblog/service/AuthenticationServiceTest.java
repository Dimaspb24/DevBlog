package com.project.devblog.service;

import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import static java.lang.String.format;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationService authenticationService;
    @Mock
    UserService userService;
    @Mock
    AuthenticationManager authenticationManager;

    UserEntity user;
    AuthenticationResponse authenticationResponse;

    @BeforeEach
    void init() {
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

        when(userService.findByLogin(user.getLogin())).thenReturn(user);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        AuthenticationResponse response = authenticationService.login(user.getLogin(), user.getPassword());

        assertThat(response.getId()).isEqualTo(authenticationResponse.getId());
        assertThat(response.getLogin()).isEqualTo(authenticationResponse.getLogin());
        assertThat(response.getRole()).isEqualTo(authenticationResponse.getRole());
    }

    @Test
    void loginTestWithNotFoundUser() throws Exception {
        when(userService.findByLogin(user.getLogin())).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> authenticationService.login(user.getLogin(), user.getPassword()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("The account with login %s does not exist", user.getLogin()));
    }

    @Test
    void loginTestWithBlockedUser() throws Exception {
        user.setEnabled(false);
        user.setVerificationCode(null);

        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        assertThatThrownBy(() -> authenticationService.login(user.getLogin(), user.getPassword()))
                .isInstanceOf(LockedException.class)
                .hasMessageContaining("This account is blocked");
    }

    @Test
    void loginTestWithNotVerifiedUser() throws Exception {
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());

        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        assertThatThrownBy(() -> authenticationService.login(user.getLogin(), user.getPassword()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("This account is not verified");
    }

    @Test
    void registerTest() throws Exception {
        when(userService.isExists(user.getLogin())).thenReturn(false);
        when(userService.register(user.getLogin(), user.getPassword())).thenReturn(user);

        AuthenticationResponse response = authenticationService.register(user.getLogin(), user.getPassword());

        assertThat(response.getId()).isEqualTo(authenticationResponse.getId());
        assertThat(response.getLogin()).isEqualTo(authenticationResponse.getLogin());
        assertThat(response.getRole()).isEqualTo(authenticationResponse.getRole());
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
