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
    List<ArticleEntity> findByTitleContains(@NonNull String title);

    Optional<ArticleEntity> findByIdAndAuthorIdAndEnabledIsTrue(@NonNull Integer articleId, @NonNull String authorId);

    Optional<ArticleEntity> findByIdAndAuthorId(@NonNull Integer articleId, @NonNull String authorId);

    Page<ArticleEntity> findByAuthorIdAndEnabledIsTrue(@NonNull String authorId, @NonNull Pageable pageable);

    @Query("select a from ArticleEntity a " +
            "where a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true and a.publicationDate is not null")
    Page<ArticleEntity> findByEnabledIsTrueAndPublicationDateIsNotNull(@NonNull Pageable pageable);

    Page<ArticleEntity> findArticleEntitiesByTitleContains(@NonNull String name, @NonNull Pageable pageable);

    @Query("select a from TagEntity t " +
            "join t.articles a " +
            "where t.name = :tagName " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null " +
            "order by a.publicationDate")
    Page<ArticleEntity> findByTagName(@NonNull @Param("tagName") String tagName, @NonNull Pageable pageable);

    @Query("select a from TagEntity t " +
            "join t.articles a " +
            "where t.name = :tagName " +
            "and lower(a.title) like lower(concat('%', :titleContains, '%'))  " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null " +
            "order by a.publicationDate")
    Page<ArticleEntity> findByTagNameAndTitleContains(@NonNull @Param("tagName") String tagName,
                                                      @NonNull @Param("titleContains") String titleContains,
                                                      @NonNull Pageable pageable);

    @Query("select a from UserEntity u " +
            "join u.subscriptions sub " +
            "join sub.selfArticles a " +
            "where u.id = :userId " +
            "and a.status = com.project.devblog.model.enums.StatusArticle.PUBLISHED " +
            "and a.enabled = true " +
            "and a.publicationDate is not null " +
            "order by a.publicationDate")
    Page<ArticleEntity> findBySubscriptions(@NonNull @Param("userId") String userId, @NonNull Pageable pageable);
}
