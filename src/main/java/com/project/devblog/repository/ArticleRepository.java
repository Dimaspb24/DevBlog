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
    Optional<ArticleEntity> findByIdAndAuthorIdAndEnabledIsTrue(@NonNull Integer id, @NonNull Integer authorId);
    @NonNull
    Page<ArticleEntity> findByAuthorIdAndEnabledIsTrue(@NonNull Integer authorId, @NonNull Pageable pageable);
    @NonNull
    @Query("select a, avg(ua.rating) as rating from ArticleEntity a\n" +
            "left outer join UserArticleEntity ua on a.id = ua.article.id\n" +
            "group by a\n")
    List<ArticleEntity> findOrderedByRating(@NonNull Sort sort);
}
