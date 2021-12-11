package com.project.devblog.repository;

import com.project.devblog.model.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {
    List<ArticleEntity> findByTitleContains(String title);
}
