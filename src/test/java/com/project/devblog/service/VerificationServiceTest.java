package com.project.devblog.service;

import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VerificationServiceTest {

    @Autowired
    private VerificationService verificationService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void sendVerificationEmailTestWithInvalidEmail() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String login = "user1@mail/$*\n";
        final String password = "pass1";
        final String verificationCode = UUID.randomUUID().toString();

        UserEntity user = UserEntity.builder()
                .id(id)
                .login(login)
                .password(password)
                .role(Role.USER)
                .enabled(true)
                .verificationCode(verificationCode)
                .build();

        Mockito.when(userRepository.getById(id))
                .thenReturn(user);

        assertThatThrownBy(() -> {
            verificationService.sendVerificationEmail(id);
        })
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("Error sending the verification message to the mail");
    }

    @Test
    void verifyTest() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String login = "user1@gmail.com";
        final String password = "pass1";
        final String verificationCode = UUID.randomUUID().toString();

        UserEntity user = UserEntity.builder()
                .id(id)
                .login(login)
                .password(password)
                .role(Role.USER)
                .enabled(false)
                .verificationCode(verificationCode)
                .build();

        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        verificationService.verify(id, verificationCode);

        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getVerificationCode()).isNull();
    }

    @Test
    void verifyTestWithAlreadyVerifiedUser() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String login = "user1@gmail.com";
        final String password = "pass1";

        UserEntity user = UserEntity.builder()
                .id(id)
                .login(login)
                .password(password)
                .role(Role.USER)
                .enabled(true)
                .verificationCode(null)
                .build();

        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        final String verificationCode = UUID.randomUUID().toString();

        assertThatThrownBy(() -> {
            verificationService.verify(id, verificationCode);
        })
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("User already verified");
    }

    @Test
    void verifyTestWithInvalidVerificationCode() throws Exception {
        final String id = UUID.randomUUID().toString();
        final String login = "user1@gmail.com";
        final String password = "pass1";
        final String verificationCode = UUID.randomUUID().toString();

        UserEntity user = UserEntity.builder()
                .id(id)
                .login(login)
                .password(password)
                .role(Role.USER)
                .enabled(false)
                .verificationCode(verificationCode)
                .build();

        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        final String invalidVerificationCode = UUID.randomUUID().toString();

        assertThatThrownBy(() -> {
            verificationService.verify(id, invalidVerificationCode);
        })
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("Invalid verification code");
    }
}
