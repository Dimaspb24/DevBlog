package com.project.devblog.repository;

import com.project.devblog.model.ArticleEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {
    @NonNull
    List<ArticleEntity> findByTitleContains(@NonNull String title);

    @NonNull
    Optional<ArticleEntity> findByIdAndAuthorIdAndEnabledIsTrue(@NonNull Integer articleId, @NonNull Integer authorId);

    @NonNull
    Page<ArticleEntity> findByAuthorIdAndEnabledIsTrue(@NonNull Integer authorId, @NonNull Pageable pageable);

    @NonNull
    @Query("select a from ArticleEntity a " +
            "where a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true and a.publicationDate is not null")
    Page<ArticleEntity> findByEnabledIsTrueAndPublicationDateIsNotNull(@NonNull Pageable pageable);

    @NonNull
    @Query("select t from TagEntity t " +
            "join t.articles a " +
            "where t.name = :name " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null " +
            "order by a.publicationDate")
    Page<ArticleEntity> findByTagName(@Param("name") String name, @NonNull Pageable pageable);
}
