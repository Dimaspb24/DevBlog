package com.project.devblog.service;

import com.project.devblog.dto.request.UserRequest;
import com.project.devblog.exception.NonUniqueValueException;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.idgenerator.Generator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Generator idGenerator;
    private final VerificationService verificationService;
    private static final Logger log = Logger.getLogger(UserService.class);


    @NonNull
    @Transactional
    public UserEntity register(@NonNull String login, @NonNull String password) {
        log.trace("The UserService register method was called with login={" + login + "}");

        String userId = idGenerator.generateId();
        final UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .login(login)
                .role(Role.USER)
                .password(passwordEncoder.encode(password))
                .verificationCode(UUID.randomUUID().toString())
                .build();

        UserEntity savedUser = userRepository.save(userEntity);

        verificationService.sendVerificationEmail(userEntity.getId());
        return savedUser;
    }

    @NonNull
    @Transactional
    public UserEntity create(@NonNull String id, @NonNull String login, @NonNull Role role, @NonNull Boolean enabled,
                             @Nullable String firstname, @Nullable String lastname, @Nullable String nickname,
                             @Nullable String photo, @Nullable String phone) {
        log.trace("The UserService creating method was called with login={" + login + "} and nickname={" + nickname + "}");

        String encodePassword = passwordEncoder.encode(UUID.randomUUID().toString());
        String verificationCode = Boolean.TRUE.equals((enabled)) ? null : UUID.randomUUID().toString();
        final UserEntity userEntity = new UserEntity(id, login, encodePassword, role, enabled, verificationCode);
        final PersonalInfo personalInfo = new PersonalInfo(firstname, lastname, nickname, photo, null, phone);
        userEntity.setPersonalInfo(personalInfo);

        log.debug("User was created with PersonalInfo={" + personalInfo + "}");

        return userRepository.findById(id)
                .orElseGet(() -> userRepository.save(userEntity));
    }

    @NonNull
    @Transactional
    public UserEntity save(UserEntity userEntity) {
        log.trace("The UserService saving user method was called");

        return userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> findAll(Pageable pageable) {
        log.trace("The UserService search method for all users was called");

        return userRepository.findAll(pageable);
    }

    @Transactional
    public UserEntity findByLogin(@NonNull String login) {
        log.trace("The UserService search method was called with login={" + login + "}");

        return userRepository.findByLogin(login).orElseThrow(() -> {
            log.error("The user with login={" + login + "} was NOT FOUND");
            return new NotFoundException(UserEntity.class, "login", login);
        });
    }

    @Transactional(readOnly = true)
    public UserEntity findById(@NonNull String id) {
        log.trace("The UserService search method was called with id={" + id + "}");

        return userRepository.findById(id).orElseThrow(() -> {
            log.error("The user with id={" + id + "} was NOT FOUND");
            return new NotFoundException(UserEntity.class, id);
        });
    }

    @Transactional(readOnly = true)
    public boolean isExists(@NonNull String login) {
        log.trace("The UserService check existing user method was called with login={" + login + "}");

        return userRepository.existsByLogin(login);
    }

    @Transactional
    public void delete(String userId) {
        log.trace("The UserService deleting user method was called with userId={" + userId + "}");

        UserEntity user = findById(userId);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public void enable(@NonNull String userId, @NonNull Boolean enabled) {
        log.trace("The UserService block/unblock user method was called for userId={" + userId + "}");

        UserEntity user = findById(userId);
        user.setEnabled(enabled);

        log.debug("User with userId={" + userId + "} was " + (enabled ? "unblocked" : "blocked"));
    }

    @Transactional
    public UserEntity update(String userId, UserRequest userRequest) {
        log.trace("The UserService updating user method was called for userId={" + userId + "}");

        UserEntity user = findById(userId);
        PersonalInfo personalInfo = user.getPersonalInfo();

        acceptIfNotNullAndNotEmpty(userRequest.getPhoto(), personalInfo::setPhoto);
        acceptIfNotNullAndNotEmpty(userRequest.getFirstname(), personalInfo::setFirstname);
        acceptIfNotNullAndNotEmpty(userRequest.getLastname(), personalInfo::setLastname);
        acceptIfNotNullAndNotEmpty(userRequest.getInfo(), personalInfo::setInfo);

        acceptIfNotNullAndNotEmpty(userRequest.getNickname(), nick -> {
            if (!userRepository.existsByPersonalInfoNickname(nick)) {
                personalInfo.setNickname(nick);
            } else {
                throw new NonUniqueValueException(format("User with nickname=%s already exists", nick));
            }
        });

        acceptIfNotNullAndNotEmpty(userRequest.getPhone(), phone -> {
            if (!userRepository.existsByPersonalInfoPhone(phone)) {
                personalInfo.setPhone(phone);
            } else {
                throw new NonUniqueValueException(format("User with this phone=%s already exists", phone));
            }
        });

        user.setPersonalInfo(personalInfo);
        userRepository.save(user);

        log.debug("User was updated with PersonalInfo={" + personalInfo + "}");

        return user;
    }

    void acceptIfNotNullAndNotEmpty(String field, Consumer<String> consumer) {
        if (Objects.nonNull(field) && !field.isEmpty()) {
            consumer.accept(field);
        }
    }
}
