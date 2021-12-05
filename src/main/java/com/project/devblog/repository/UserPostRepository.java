package com.project.devblog.repository;

import com.project.devblog.model.UserPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends JpaRepository<UserPostEntity, Long> {
}
