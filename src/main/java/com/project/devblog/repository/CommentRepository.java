package com.project.devblog.repository;

import com.project.devblog.model.CommentEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByArticleIdAndEnabledIsTrue(@NonNull Integer articleId, @NonNull Pageable pageable);

    Page<CommentEntity> findAllByAuthorIdAndArticleIdAndEnabledIsTrue(@NonNull String authorId, @NonNull Integer articleId, @NonNull Pageable pageable);

    Optional<CommentEntity> findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(@NonNull Long commentId, @NonNull String authorId, @NonNull Integer articleId);

    Optional<CommentEntity> findByIdAndAuthorIdAndArticleId(@NonNull Long id, @NonNull String authorId, @NonNull Integer articleId);
}
