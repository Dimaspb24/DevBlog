package com.project.devblog.service;

import com.project.devblog.model.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity register(UserEntity user);

    List<UserEntity> getAll();

    UserEntity findByLogin(String login);

    UserEntity findById(Long id);

    void delete(Long id);
}
