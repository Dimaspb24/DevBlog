package com.project.devblog.service;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final BCryptPasswordEncoder passwordEncoder;

    @NonNull
    public UserEntity register(@NonNull String login, @NonNull String password,
                               @NonNull Role role, @NonNull StatusUser status) {

        final UserEntity userEntity = new UserEntity(login, role, status);
        userEntity.setPassword(passwordEncoder.encode(password));

        return userRepository.save(userEntity);
    }

    public boolean isExists(@NonNull String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    @NonNull
    public UserEntity findByLogin(@NonNull String login) {
        return userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public UserEntity get(@NonNull Integer id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @NonNull
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public void delete(@NonNull Integer id) {
        userRepository.deleteById(id);
    }
}


