package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.service.exception.ArticleConflictException;
import com.project.devblog.service.exception.ArticleNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ArticleService {

    @NonNull
    private final ArticleRepository articleRepository;
    @NonNull
    private final UserService userService;
    @NonNull
    private final TagService tagService;

    @NonNull
    public ArticleEntity get(@NonNull Integer articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
    }

    @NonNull
    public ArticleEntity get(@NonNull String authorId, @NonNull Integer articleId) {
        return articleRepository.findByIdAndAuthorIdAndEnabledIsTrue(articleId, authorId).orElseThrow(ArticleNotFoundException::new);
    }

    @NonNull
    public ArticleEntity findById(@NonNull Integer articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
    }

    @NonNull
    public ArticleEntity create(@NonNull String userId, @NonNull String title, List<String> tags,
                                @NonNull String description, @NonNull String body, @NonNull StatusArticle status) {

        final UserEntity author = userService.get(userId);
        final ArticleEntity articleEntity = new ArticleEntity(title, body, status, description, author);

        if (status.name().equalsIgnoreCase(StatusArticle.PUBLISHED.name())) {
            final LocalDateTime now = LocalDateTime.now();
            articleEntity.setPublicationDate(now);
        }

        if (tags != null) {
            final List<TagEntity> tagEntities = tagService.createAndGetAllByName(tags);
            articleEntity.setTags(tagEntities);
        }

        return articleRepository.save(articleEntity);
    }

    @NonNull
    public Page<ArticleEntity> getAll(@NonNull String userId, @NonNull Pageable pageable) {
        return articleRepository.findByAuthorIdAndEnabledIsTrue(userId, pageable);
    }

    public void enable(@NonNull String authorId, @NonNull Integer articleId, @NonNull Boolean enabled) {
        final ArticleEntity articleEntity = articleRepository.findByIdAndAuthorId(articleId, authorId)
                .orElseThrow(ArticleNotFoundException::new);

        if (enabled.equals(articleEntity.getEnabled())) {
            String message = enabled ? "Article is already enabled" : "Article is already disabled";
            throw new ArticleConflictException(message);
        }

        articleEntity.setEnabled(enabled);
        articleRepository.save(articleEntity);
    }

    public void delete(@NonNull String authorId, @NonNull Integer articleId) {
        final ArticleEntity articleEntity = articleRepository.findByIdAndAuthorId(articleId, authorId)
                .orElseThrow(ArticleNotFoundException::new);
        articleRepository.delete(articleEntity);
    }

    @NonNull
    public ArticleEntity update(@NonNull String authorId, @NonNull Integer articleId, @NonNull String title, List<String> tags,
                                @NonNull String description, @NonNull String body, @NonNull StatusArticle status) {
        final ArticleEntity articleEntity = get(authorId, articleId);
        final List<TagEntity> tagEntities = tagService.createAndGetAllByName(tags);

        articleEntity.setTitle(title);
        articleEntity.setTags(tagEntities);
        articleEntity.setDescription(description);
        articleEntity.setBody(body);

        if (status == StatusArticle.PUBLISHED && articleEntity.getStatus() != StatusArticle.PUBLISHED) {
            articleEntity.setPublicationDate(LocalDateTime.now());
        }

        articleEntity.setStatus(status);

        return articleRepository.save(articleEntity);
    }

    @NonNull
    public Page<ArticleEntity> getArticlesBySort(@NonNull Pageable pageable) {
        return articleRepository.findByEnabledIsTrueAndPublicationDateIsNotNull(pageable);
    }

    @NonNull
    public Page<ArticleEntity> getByTitleName(String name, @NonNull Pageable pageable) {
        if (name == null || name.isEmpty()) {
            return articleRepository.findByEnabledIsTrueAndPublicationDateIsNotNull(pageable);
        }
        return articleRepository.findArticleEntitiesByTitleContains(name, pageable);
    }

    @NonNull
    public Page<ArticleEntity> findArticlesByTagName(@NonNull String tagName, @NonNull Pageable pageable) {
        return articleRepository.findByTagName(tagName, pageable);
    }

    @NonNull
    public Page<ArticleEntity> findArticlesBySubscriptions(@NonNull String userId, @NonNull Pageable pageable) {
        return articleRepository.findBySubscriptions(userId, pageable);
    }
}
