package com.project.devblog.repository;

import com.project.devblog.model.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @NonNull
    Optional<UserEntity> findByLogin(@NonNull String login);
}
