package com.project.devblog.repository;

import com.project.devblog.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByLogin(String login);
}
