package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VerificationServiceTest {

    @Autowired
    private VerificationService verificationService;
    @MockBean
    private UserRepository userRepository;
    private static UserEntity user;

    @BeforeAll
    static void init() {
        user = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .login("user1@gmail.com")
                .password("pass1")
                .role(Role.USER)
                .build();
    }

    @Test
    void sendVerificationEmailTestWithInvalidEmail() throws Exception {
        user.setLogin("user1@mail/$*\n");

        when(userRepository.getById(user.getId()))
                .thenReturn(user);

        assertThatThrownBy(() -> verificationService.sendVerificationEmail(user.getId()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("Error sending the verification message to the mail");
    }

    @Test
    void verifyTest() throws Exception {
        user.setLogin("user1@gmail.com");
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user))
                .thenReturn(user);

        verificationService.verify(user.getId(), user.getVerificationCode());

        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getVerificationCode()).isNull();
    }

    @Test
    void verifyTestWithNotFoundUser() throws Exception {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());

        final String verificationCode = UUID.randomUUID().toString();
        assertThatThrownBy(() -> verificationService.verify(user.getId(), verificationCode))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("%s with id=%s not found", UserEntity.class.getSimpleName(), user.getId()));
    }

    @Test
    void verifyTestWithAlreadyVerifiedUser() throws Exception {
        user.setLogin("user1@gmail.com");
        user.setEnabled(true);
        user.setVerificationCode(null);

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user))
                .thenReturn(user);

        final String verificationCode = UUID.randomUUID().toString();

        assertThatThrownBy(() -> verificationService.verify(user.getId(), verificationCode))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("User already verified");
    }

    @Test
    void verifyTestWithInvalidVerificationCode() throws Exception {
        user.setLogin("user1@gmail.com");
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user))
                .thenReturn(user);

        final String invalidVerificationCode = UUID.randomUUID().toString();

        assertThatThrownBy(() -> verificationService.verify(user.getId(), invalidVerificationCode))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("Invalid verification code");
    }
}
