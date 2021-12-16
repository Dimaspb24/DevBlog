package com.project.devblog.repository;

import com.project.devblog.model.ArticleEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {
    @NonNull
    List<ArticleEntity> findByTitleContains(@NonNull String title);
    @NonNull
    Optional<ArticleEntity> findByIdAndAuthorIdAndEnabledIsTrue(@NonNull Integer id, @NonNull Integer authorId);
    @NonNull
    Page<ArticleEntity> findByAuthorIdAndEnabledIsTrue(@NonNull Integer authorId, @NonNull Pageable pageable);
}
