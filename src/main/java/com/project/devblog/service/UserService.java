package com.project.devblog.service;

import com.project.devblog.controller.dto.request.UserRequest;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.exception.UserNotFoundException;
import com.project.devblog.service.idgenerator.Generator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final BCryptPasswordEncoder passwordEncoder;
    @NonNull
    private final Generator idGenerator;

    @NonNull
    public UserEntity register(@NonNull String login, @NonNull String password,
                               @NonNull Role role, @NonNull StatusUser status) {

        final UserEntity userEntity = new UserEntity(idGenerator.generateId(), login, role, status);
        userEntity.setPassword(passwordEncoder.encode(password));

        return userRepository.save(userEntity);
    }

    @NonNull
    public UserEntity createUser(@NonNull String id, @NonNull String login, @NonNull Role role, @NonNull StatusUser status,
                                 @Nullable String firstname, @Nullable String lastname, @Nullable String nickname,
                                 @Nullable String photo, @Nullable String phone) {
        final UserEntity userEntity = new UserEntity(id, login, role, status);
        final PersonalInfo personalInfo = new PersonalInfo(firstname, lastname, nickname, photo, null, phone, true);
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
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public UserEntity get(@NonNull String id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void delete(String userId) {
        UserEntity user = get(userId);
        user.setStatus(StatusUser.BANNED);
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
