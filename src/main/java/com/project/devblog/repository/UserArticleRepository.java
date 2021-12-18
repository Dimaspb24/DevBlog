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
    @NonNull
    Optional<UserArticleEntity> findByUserIdAndArticleIdAndArticleEnabledIsTrue(@NonNull Integer userId, @NonNull Integer articleId);

    @NonNull
    Optional<UserArticleEntity> findByUserIdAndArticleId(@NonNull Integer userId, @NonNull Integer articleId);

    @NonNull
    Page<UserArticleEntity> findByUserIdAndBookmarkType(@NonNull Integer userId, @NonNull BookmarkType bookmarkType, Pageable pageable);

}
