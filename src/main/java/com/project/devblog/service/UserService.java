package com.project.devblog.service;

import com.project.devblog.controller.dto.request.UserRequest;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.idgenerator.Generator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final BCryptPasswordEncoder passwordEncoder;
    @NonNull
    private final Generator idGenerator;
    @NonNull
    private final VerificationService verificationService;

    @NonNull
    public UserEntity register(@NonNull String login, @NonNull String password) {
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
    public UserEntity createUser(@NonNull String id, @NonNull String login, @NonNull Role role, @NonNull Boolean enabled,
                                 @Nullable String firstname, @Nullable String lastname, @Nullable String nickname,
                                 @Nullable String photo, @Nullable String phone) {
        final UserEntity userEntity = new UserEntity(id, login, passwordEncoder.encode(UUID.randomUUID().toString()), role, enabled);
        final PersonalInfo personalInfo = new PersonalInfo(firstname, lastname, nickname, photo, null, phone);
        userEntity.setPersonalInfo(personalInfo);

        final Optional<UserEntity> userOptional = userRepository.findById(id);

        return userOptional.orElseGet(() -> userRepository.save(userEntity));
    }

    public boolean isExists(@NonNull String login) {
        return userRepository.existsByLogin(login);
    }

    @NonNull
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @NonNull
    public UserEntity findByLogin(@NonNull String login) {
        return userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(UserEntity.class, "login", login));
    }

    @NonNull
    public UserEntity get(@NonNull String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(UserEntity.class, id));
    }

    public void delete(String userId) {
        UserEntity user = get(userId);
        user.setEnabled(false);
        userRepository.save(user);
    }

    public UserEntity update(String userId, UserRequest userRequest) {
        UserEntity user = get(userId);

        String firstname = userRequest.getFirstname();
        if (firstname != null && !firstname.isEmpty()) {
            user.getPersonalInfo().setFirstname(firstname);
        }

        String lastname = userRequest.getLastname();
        if (lastname != null && !lastname.isEmpty()) {
            user.getPersonalInfo().setLastname(lastname);
        }

        String phone = userRequest.getPhone();
        if (phone != null && !phone.isEmpty() && !userRepository.existsByPersonalInfoPhone(phone)) {
            user.getPersonalInfo().setPhone(phone);
        }

        String info = userRequest.getInfo();
        if (info != null && !info.isEmpty()) {
            user.getPersonalInfo().setInfo(info);
        }

        String nickname = userRequest.getNickname();
        if (nickname != null && !nickname.isEmpty() && !userRepository.existsByPersonalInfoNickname(nickname)) {
            user.getPersonalInfo().setNickname(nickname);
        }

        userRepository.save(user);
        return user;
    }
}
