package com.project.devblog.repository;

import com.project.devblog.model.UserArticleEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserArticleRepository extends JpaRepository<UserArticleEntity, Integer> {
    @NonNull
    Optional<UserArticleEntity> findByUserIdAndArticleIdAndArticleEnabledIsTrue(@NonNull Integer userId, @NonNull Integer articleId);
}
