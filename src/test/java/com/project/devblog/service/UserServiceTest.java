package com.project.devblog.service;

import com.project.devblog.dto.request.UserRequest;
import com.project.devblog.exception.NonUniqueValueException;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.idgenerator.Generator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    Generator idGenerator;
    @Mock
    VerificationService verificationService;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;
    @Captor
    ArgumentCaptor<UserEntity> argumentCaptorUser;

    UserEntity user;
    PersonalInfo personalInfo;

    @BeforeEach
    void init() {
        user = UserEntity.builder()
                .id("generatedId")
                .role(Role.USER)
                .login("login@mail.ru")
                .password("encodedPassword")
                .verificationCode(UUID.randomUUID().toString())
                .build();
        personalInfo = PersonalInfo.builder()
                .firstname("Ivan")
                .nickname("NickIvan")
                .phone("89999999999")
                .info("Some info")
                .lastname("Ivanov")
                .photo("https://...")
                .build();
    }

    @Test
    void shouldCreateUserAndSendEmailWhenRegister() {
        String password = "password";
        doReturn(user.getId()).when(idGenerator).generateId();
        doReturn(user.getPassword()).when(passwordEncoder).encode(password);
        doReturn(user).when(userRepository).save(any(UserEntity.class));
        doNothing().when(verificationService).sendVerificationEmail(user.getId());

        UserEntity registeredUser = userService.register(user.getLogin(), password);

        assertAll(
                () -> assertThat(registeredUser).hasNoNullFieldsOrPropertiesExcept("personalInfo", "creationDate", "modificationDate"),
                () -> assertThat(registeredUser.getId()).isEqualTo(user.getId()),
                () -> assertThat(registeredUser.getPassword()).isEqualTo(user.getPassword()),
                () -> assertThat(registeredUser.getLogin()).isEqualTo(user.getLogin()),
                () -> assertThat(registeredUser.getRole()).isEqualTo(Role.USER)
        );

        verify(idGenerator).generateId();
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(UserEntity.class));
        verify(verificationService).sendVerificationEmail(user.getId());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void shouldCreateUserWithPersonalInfo(Boolean enabled) {
        user.setPersonalInfo(personalInfo);

        doReturn(user.getPassword()).when(passwordEncoder).encode(anyString());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        UserEntity actualNewUser = userService.createUser(user.getId(), user.getLogin(), user.getRole(), enabled,
                personalInfo.getFirstname(), personalInfo.getLastname(), personalInfo.getNickname(),
                personalInfo.getPhoto(), personalInfo.getPhone());

        assertThat(actualNewUser).isEqualTo(user);

        verify(passwordEncoder).encode(anyString());
        verify(userRepository).findById(user.getId());
        verify(userRepository).save(argumentCaptorUser.capture());

        if (enabled) {
            assertThat(argumentCaptorUser.getValue().getVerificationCode()).isNull();
        } else {
            assertThat(argumentCaptorUser.getValue().getVerificationCode()).isNotNull();
        }
    }

    @Test
    void shouldReturnTrueIfUserExists() {
        doReturn(true).when(userRepository).existsByLogin(user.getLogin());

        boolean actualResult = userService.isExists(user.getLogin());
        assertTrue(actualResult);

        verify(userRepository).existsByLogin(user.getLogin());
    }

    @Test
    void shouldEnabledIsFalseAfterSoftDelete() {
        doReturn(Optional.of(user)).when(userRepository).findById(user.getId());

        userService.delete(user.getId());

        verify(userRepository).save(argumentCaptorUser.capture());
        assertThat(argumentCaptorUser.getValue().isEnabled()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void shouldUpdateEnabled(Boolean enabled) {
        doReturn(Optional.of(user)).when(userRepository).findById(user.getId());

        userService.enable(user.getId(), enabled);

        verify(userRepository).save(argumentCaptorUser.capture());
        assertThat(argumentCaptorUser.getValue().isEnabled()).isEqualTo(enabled);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/update-user-test-data.csv", delimiter = ';', numLinesToSkip = 1)
    void shouldUpdateUserIfFieldsNotNullAndNotEmpty(String nickname, String phone, String photo, String firstname, String lastname, String info) {
        user.setPersonalInfo(personalInfo);
        UserRequest userRequest = new UserRequest(phone, firstname, lastname, nickname, info, photo);

        acceptIfNotNullAndNotEmpty(firstname, s -> personalInfo.setFirstname(s));
        acceptIfNotNullAndNotEmpty(lastname, s -> personalInfo.setLastname(s));
        acceptIfNotNullAndNotEmpty(info, s -> personalInfo.setInfo(s));
        acceptIfNotNullAndNotEmpty(photo, s -> personalInfo.setPhoto(s));

        doReturn(Optional.of(user)).when(userRepository).findById(user.getId());
        lenient().when(userRepository.existsByPersonalInfoNickname(user.getPersonalInfo().getNickname())).thenReturn(true);
        lenient().when(userRepository.existsByPersonalInfoPhone(user.getPersonalInfo().getPhone())).thenReturn(true);

        if (personalInfo.getNickname().equals(nickname) || personalInfo.getPhone().equals(phone)) {
            assertThrows(NonUniqueValueException.class, () -> userService.update(user.getId(), userRequest));
            if (personalInfo.getPhone().equals(phone)) {
                verify(userRepository).existsByPersonalInfoPhone(phone);
            } else {
                verify(userRepository).existsByPersonalInfoNickname(nickname);
            }
        } else {
            userService.update(user.getId(), userRequest);

            acceptIfNotNullAndNotEmpty(nickname, s -> verify(userRepository).existsByPersonalInfoNickname(nickname));
            acceptIfNotNullAndNotEmpty(phone, s -> verify(userRepository).existsByPersonalInfoPhone(phone));

            verify(userRepository).save(argumentCaptorUser.capture());
            assertThat(argumentCaptorUser.getValue().getPersonalInfo()).isEqualTo(personalInfo);
        }
    }

    @Test
    void shouldThrowIfNotFoundUserByLogin() {
        doReturn(Optional.empty()).when(userRepository).findByLogin(anyString());
        assertThrows(NotFoundException.class, () -> userService.findByLogin(UUID.randomUUID().toString()));
    }

    @Test
    void shouldThrowIfNotFoundUserById() {
        doReturn(Optional.empty()).when(userRepository).findById(anyString());
        assertThrows(NotFoundException.class, () -> userService.find(UUID.randomUUID().toString()));
    }

    void acceptIfNotNullAndNotEmpty(String field, Consumer<String> consumer) {
        if (Objects.nonNull(field) && !field.isEmpty()) {
            consumer.accept(field);
        }
    }
}