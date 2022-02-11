package com.project.devblog.repository;

import com.project.devblog.model.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByLogin(@NonNull String login);

    @NonNull
    Boolean existsByPersonalInfoNickname(@NonNull String nickname);

    @NonNull
    Boolean existsByPersonalInfoPhone(@NonNull String phone);

    @NonNull
    Boolean existsByLogin(@NonNull String login);
}
