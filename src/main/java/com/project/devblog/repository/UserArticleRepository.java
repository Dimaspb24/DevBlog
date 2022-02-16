package com.project.devblog.repository;

import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.enums.BookmarkType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserArticleRepository extends JpaRepository<UserArticleEntity, Long> {
    Optional<UserArticleEntity> findByUserIdAndArticleIdAndArticleEnabledIsTrue(@NonNull String userId, @NonNull Integer articleId);

    Optional<UserArticleEntity> findByUserIdAndArticleId(@NonNull String userId, @NonNull Integer articleId);

    Page<UserArticleEntity> findByUserIdAndBookmarkType(@NonNull String userId, @NonNull BookmarkType bookmarkType, Pageable pageable);

    Page<UserArticleEntity> findByUserIdAndBookmarkTypeNotNull(@NonNull String userId, Pageable pageable);
}
