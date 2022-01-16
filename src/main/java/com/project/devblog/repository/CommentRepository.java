package com.project.devblog.repository;

import com.project.devblog.model.CommentEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    @NonNull
    Page<CommentEntity> findAllByArticleIdAndAuthorIdAndEnabledIsTrue(@NonNull String articleId, @NonNull String authorId, @NonNull Pageable pageable);

    @NonNull
    Optional<CommentEntity> findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(@NonNull String id, @NonNull String authorId, @NonNull String articleId);
}
