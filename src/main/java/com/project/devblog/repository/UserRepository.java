package com.project.devblog.repository;

import com.project.devblog.model.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @NonNull
    Optional<UserEntity> findByLogin(@NonNull String login);

    @NonNull
    @Query("SELECT u FROM UserEntity u WHERE u.verificationCode = :code")
    Optional<UserEntity> findByVerificationCode(@NonNull @Param("code") String verificationCode);

    @NonNull
    Boolean existsByPersonalInfoNickname(String nickname);

    @NonNull
    Boolean existsByPersonalInfoPhone(String phone);

    @NonNull
    Boolean existsByLogin(String login);
}
