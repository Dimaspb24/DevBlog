package com.project.devblog.repository;

import com.project.devblog.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByLogin(String login);

    Boolean existsByPersonalInfoNickname(String nickname);
}
