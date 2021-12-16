package com.project.devblog.repository;

import com.project.devblog.model.CommentEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    @NonNull
    Page<CommentEntity> findAllByArticleIdAndAuthorIdAndEnabledIsTrue(@NonNull Integer articleId, @NonNull Integer authorId, @NonNull Pageable pageable);

    @NonNull
    Optional<CommentEntity> findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(@NonNull Integer id, @NonNull Integer authorId, @NonNull Integer articleId);

    void delete(@NonNull Integer id);
}
