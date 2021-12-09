package com.project.devblog.repository;

import com.project.devblog.model.UserArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserArticleRepository extends JpaRepository<UserArticleEntity, Long> {
}
