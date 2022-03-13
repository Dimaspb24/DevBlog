package com.project.devblog.service;

import com.project.devblog.config.MailProperties;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    UserRepository userRepository;
    @Spy
    JavaMailSender mailSender;
    @Mock
    MailProperties mailProperties;
    @InjectMocks
    VerificationService verificationService;

    UserEntity user;

    @BeforeEach
    void init() {
        user = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .login("user1@gmail.com")
                .password("pass1")
                .role(Role.USER)
                .build();
    }

    @Test
    void sendVerificationEmailWithInvalidEmail() {
        String name = "IDK";
        String email = "idkspbcorp.com";
        String url = "http://localhost:8080/v1/users/[[userId]]/verify?code=[[code]]";
        String subject = "Подтверждение регистрации";
        String content = "Приветствуем, [[name]]!<br>\n" +
                "Для подтверждения электронной почты и завершения процесса регистрации,\n" +
                "пожалуйста, пройдите по ссылке:<br>\n" +
                "<h3><a href=\"[[URL]]\" target=\"_self\">Подтвердить регистрацию</a></h3>\n" +
                "Если вы получили это письмо по ошибке, просто игнорируйте его.";

        user.setLogin("user1@mail/$*\n");
        user.setVerificationCode("123");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doReturn(content).when(mailProperties).getContent();
        doReturn(url).when(mailProperties).getUrl();
        doReturn(email).when(mailProperties).getEmail();
        doReturn(name).when(mailProperties).getName();
        doReturn(subject).when(mailProperties).getSubject();
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        assertThatThrownBy(() -> verificationService.sendVerificationEmail(user.getId()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("Error sending the verification message to the mail");
    }

    @Test
    void verifySuccess() {
        user.setLogin("user1@gmail.com");
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        verificationService.verify(user.getId(), user.getVerificationCode());

        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getVerificationCode()).isNull();
    }

    @Test
    void verifyWithAlreadyVerifiedUser() {
        user.setLogin("user1@gmail.com");
        user.setEnabled(true);
        user.setVerificationCode(null);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> verificationService.verify(user.getId(), UUID.randomUUID().toString()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("User already verified");
    }

    @Test
    void verifyWithInvalidVerificationCode() {
        user.setLogin("user1@gmail.com");
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> verificationService.verify(user.getId(), UUID.randomUUID().toString()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("Invalid verification code");
    }
}
