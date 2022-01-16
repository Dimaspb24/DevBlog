package com.project.devblog.repository;

import com.project.devblog.model.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    @NonNull
    Optional<UserEntity> findByLogin(@NonNull String login);

    @NonNull
    Boolean existsByPersonalInfoNickname(String nickname);

    @NonNull
    Boolean existsByPersonalInfoPhone(String phone);

    @NonNull
    Boolean existsByLogin(String login);
}
