package com.project.devblog.repository;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserArticleRepository extends JpaRepository<UserArticleEntity, Long> {
    Optional<UserArticleEntity> findByUserAndArticle(UserEntity user, ArticleEntity article);
}
