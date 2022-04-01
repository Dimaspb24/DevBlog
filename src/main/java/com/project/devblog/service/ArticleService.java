package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.repository.ArticleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.project.devblog.model.enums.StatusArticle.CREATED;
import static com.project.devblog.model.enums.StatusArticle.PUBLISHED;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final TagService tagService;

    @NonNull
    @Transactional(readOnly = true)
    public ArticleEntity findById(@NonNull Integer articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(ArticleEntity.class, articleId.toString()));
    }

    @NonNull
    @Transactional(readOnly = true)
    public ArticleEntity findByAuthorIdAndArticleId(@NonNull String authorId, @NonNull Integer articleId) {
        return articleRepository.findByIdAndAuthorIdAndEnabledIsTrue(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, "articleId", articleId.toString(), "authorId", authorId));
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<ArticleEntity> findAllEnabled(@NonNull String userId, Pageable pageable) {
        return articleRepository.findByAuthorIdAndEnabledIsTrue(userId, pageable);
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<ArticleEntity> findAllEnabled(String titleContains, String tagName, Pageable pageable) {
        Page<ArticleEntity> articleEntities;
        if (Objects.isNull(titleContains) && Objects.isNull(tagName)) {
            articleEntities = articleRepository.findByEnabledIsTrueAndPublicationDateIsNotNull(pageable);
        } else if (Objects.nonNull(titleContains) && Objects.nonNull(tagName)) {
            articleEntities = articleRepository.findByEnabledAndTagNameAndTitleContains(tagName, titleContains, pageable);
        } else if (Objects.nonNull(titleContains)) {
            articleEntities = articleRepository.findByEnabledIsTrueAndTitleContains(titleContains, pageable);
        } else {
            articleEntities = articleRepository.findByEnabledAndTagName(tagName, pageable);
        }
        return articleEntities;
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<ArticleEntity> findBySubscriptions(@NonNull String userId, Pageable pageable) {
        return articleRepository.findBySubscriptions(userId, pageable);
    }

    @NonNull
    @Transactional
    public ArticleEntity create(@NonNull String userId, @NonNull String title, List<String> tags,
                                @NonNull String description, @NonNull String body, @NonNull StatusArticle status) {

        final UserEntity author = userService.findById(userId);
        final ArticleEntity articleEntity = new ArticleEntity(title, body, status, description, author);

        if (status.toString().equalsIgnoreCase(PUBLISHED.toString())) {
            articleEntity.setPublicationDate(LocalDateTime.now());
        }

        if (tags != null) {
            articleEntity.setTags(tagService.createAndGetAllByName(tags));
        }

        return articleRepository.save(articleEntity);
    }

    @NonNull
    @Transactional
    public ArticleEntity update(@NonNull String authorId, @NonNull Integer articleId, @NonNull String title, List<String> tags,
                                @NonNull String description, @NonNull String body, @NonNull StatusArticle status) {
        final ArticleEntity articleEntity = findByAuthorIdAndArticleId(authorId, articleId);
        final List<TagEntity> tagEntities = tagService.createAndGetAllByName(tags);

        articleEntity.setTitle(title);
        articleEntity.setTags(tagEntities);
        articleEntity.setDescription(description);
        articleEntity.setBody(body);

        StatusArticle statusArticle = articleEntity.getStatus();
        if (status == PUBLISHED && statusArticle == CREATED) {
            articleEntity.setPublicationDate(LocalDateTime.now());
        }
        if (status == CREATED && statusArticle == PUBLISHED) {
            articleEntity.setPublicationDate(null);
        }
        articleEntity.setStatus(status);

        return articleRepository.save(articleEntity);
    }

    @Transactional
    public void delete(@NonNull String authorId, @NonNull Integer articleId) {
        final ArticleEntity articleEntity = articleRepository.findByIdAndAuthorId(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, "articleId", articleId.toString(), "authorId", authorId));
        articleRepository.delete(articleEntity);
    }

    @Transactional
    public void enable(@NonNull String authorId, @NonNull Integer articleId, @NonNull Boolean enabled) {
        final ArticleEntity articleEntity = articleRepository.findByIdAndAuthorId(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, "articleId", articleId.toString(), "authorId", authorId));

        articleEntity.setEnabled(enabled);
        articleRepository.save(articleEntity);
    }
}
