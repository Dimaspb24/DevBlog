package com.project.devblog.repository;

import com.project.devblog.model.ArticleEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {
    @NonNull
    List<ArticleEntity> findByTitleContains(@NonNull String title);
    @NonNull
    Optional<ArticleEntity> findByIdAndAuthorIdAndEnabledIsTrue(@NonNull Integer authorId, @NonNull Integer id);
    @NonNull
    Page<ArticleEntity> findByAuthorIdAndEnabledIsTrue(@NonNull Integer authorId, @NonNull Pageable pageable);
    @NonNull
    Page<ArticleEntity> findByEnabledIsTrue(@NonNull Pageable pageable);
}
