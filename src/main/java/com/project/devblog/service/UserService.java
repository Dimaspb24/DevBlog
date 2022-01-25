package com.project.devblog.service;

import com.project.devblog.controller.dto.request.UserRequest;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final BCryptPasswordEncoder passwordEncoder;
    @NonNull
    private final VerificationService verificationService;

    @NonNull
    public UserEntity register(@NonNull String login, @NonNull String password) {
        final UserEntity userEntity = UserEntity.builder()
                .login(login)
                .role(Role.USER)
                .password(passwordEncoder.encode(password))
                .verificationCode(UUID.randomUUID().toString())
                .build();

        userRepository.save(userEntity);

        verificationService.sendVerificationEmail(userEntity.getId());

        return userEntity;
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
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public UserEntity get(@NonNull Integer id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void delete(Integer userId) {
        UserEntity user = get(userId);
        user.setEnabled(false);
        userRepository.save(user);
    }

    public UserEntity update(Integer userId, UserRequest userRequest) {
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
