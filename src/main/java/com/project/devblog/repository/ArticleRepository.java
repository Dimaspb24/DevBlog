package com.project.devblog.repository;

import com.project.devblog.model.ArticleEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {

    Optional<ArticleEntity> findByIdAndAuthorIdAndEnabledIsTrue(@NonNull Integer articleId, @NonNull String authorId);

    Optional<ArticleEntity> findByIdAndAuthorId(@NonNull Integer articleId, @NonNull String authorId);

    Page<ArticleEntity> findByAuthorIdAndEnabledIsTrue(@NonNull String authorId, Pageable pageable);

    @Query("select a from ArticleEntity a " +
            "where a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true and a.publicationDate is not null")
    Page<ArticleEntity> findByEnabledIsTrueAndPublicationDateIsNotNull(Pageable pageable);

    @Query("select a from ArticleEntity a " +
            "where lower(a.title) like lower(concat('%', :titleContains, '%'))  " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null")
    Page<ArticleEntity> findByEnabledIsTrueAndTitleContains(@NonNull @Param("titleContains") String titleContains, Pageable pageable);

    @Query("select a from ArticleEntity a " +
            "join a.tags t " +
            "where t.name = :tagName " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null")
    Page<ArticleEntity> findByEnabledAndTagName(@NonNull @Param("tagName") String tagName, Pageable pageable);

    @Query("select a from ArticleEntity a " +
            "join a.tags t " +
            "where t.name = :tagName " +
            "and lower(a.title) like lower(concat('%', :titleContains, '%'))  " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null")
    Page<ArticleEntity> findByEnabledAndTagNameAndTitleContains(@NonNull @Param("tagName") String tagName,
                                                                @NonNull @Param("titleContains") String titleContains,
                                                                Pageable pageable);

    @Query("select a from UserEntity u " +
            "join u.subscriptions sub " +
            "join sub.selfArticles a " +
            "where u.id = :userId " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null")
    Page<ArticleEntity> findBySubscriptions(@NonNull @Param("userId") String userId, Pageable pageable);
}
