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
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {
    @NonNull
    List<ArticleEntity> findByTitleContains(@NonNull String title);

    @NonNull
    Optional<ArticleEntity> findByIdAndAuthorIdAndEnabledIsTrue(@NonNull String articleId, @NonNull String authorId);

    @NonNull
    Page<ArticleEntity> findByAuthorIdAndEnabledIsTrue(@NonNull String authorId, @NonNull Pageable pageable);

    @NonNull
    @Query("select a from ArticleEntity a " +
            "where a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true and a.publicationDate is not null")
    Page<ArticleEntity> findByEnabledIsTrueAndPublicationDateIsNotNull(@NonNull Pageable pageable);

    @NonNull
    Page<ArticleEntity> findArticleEntitiesByTitleContains(@NonNull String name, @NonNull Pageable pageable);


    @NonNull
    @Query("select a from TagEntity t " +
            "join t.articles a " +
            "where t.name = :tagName " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null " +
            "order by a.publicationDate")
    Page<ArticleEntity> findByTagName(@Param("tagName") String tagName, @NonNull Pageable pageable);

    @Query("select a from UserEntity u " +
            "join u.subscriptions sub " +
            "join sub.selfArticles a " +
            "where u.id = :userId " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null " +
            "order by a.publicationDate")
    Page<ArticleEntity> findBySubscriptions(@Param("userId") Integer userId, @NonNull Pageable pageable);
}
