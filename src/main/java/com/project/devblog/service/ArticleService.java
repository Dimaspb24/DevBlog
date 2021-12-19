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
    private final AuthUserService userService;
    @NonNull
    private final TagService tagService;

    // todo replace or delete
    @NonNull
    public ArticleEntity get(@NonNull Integer userId, @NonNull Integer articleId) {
        return articleRepository.findByIdAndAuthorIdAndEnabledIsTrue(userId, articleId).orElseThrow(ArticleNotFoundException::new);
    }

    @NonNull
    public ArticleEntity findById(@NonNull Integer articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
    }

    @NonNull
    public ArticleEntity create(@NonNull Integer userId, @NonNull String title, List<String> tags,
                                @NonNull String description, @NonNull String body, @NonNull StatusArticle status) {

        final UserEntity author = userService.get(userId);
        final ArticleEntity articleEntity = new ArticleEntity(title, body, status, description, author);

        if (status.name().equalsIgnoreCase(StatusArticle.PUBLISHED.name())) {
            final LocalDateTime now = LocalDateTime.now();
            articleEntity.setPublicationDate(now);
        }

        if (tags != null) {
            final List<TagEntity> tagEntities = tagService.getAllByName(tags);
            articleEntity.setTags(tagEntities);
        }

        return articleRepository.save(articleEntity);
    }

    @NonNull
    public Page<ArticleEntity> getAll(@NonNull Integer userId, @NonNull Pageable pageable) {
        return articleRepository.findByAuthorIdAndEnabledIsTrue(userId, pageable);
    }

    public void delete(@NonNull Integer authorId, @NonNull Integer articleId) {
        final ArticleEntity articleEntity = get(articleId, authorId);

        if (!articleEntity.getEnabled()) {
            throw new ArticleConflictException();
        }

        articleEntity.setEnabled(false);
        articleEntity.setDeletionDate(LocalDateTime.now());
        articleRepository.save(articleEntity);
    }

    @NonNull
    public ArticleEntity update(@NonNull Integer authorId, @NonNull Integer articleId, @NonNull String title, List<String> tags,
                                @NonNull String description, @NonNull String body, @NonNull StatusArticle status) {
        final ArticleEntity articleEntity = get(articleId, authorId);
        final List<TagEntity> tagEntities = tagService.getAllByName(tags);

        articleEntity.setTitle(title);
        articleEntity.setTags(tagEntities);
        articleEntity.setDescription(description);
        articleEntity.setBody(body);
        articleEntity.setStatus(status);

        return articleRepository.save(articleEntity);
    }

}
