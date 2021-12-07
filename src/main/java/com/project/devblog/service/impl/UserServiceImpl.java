package com.project.devblog.service.impl;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserEntity register(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(StatusUser.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
