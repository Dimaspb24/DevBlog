package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
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

    public static final String ARTICLE_ID_FIELD_NAME = "articleId";
    public static final String AUTHOR_ID_FIELD_NAME = "authorId";
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final TagService tagService;

    @NonNull
    @Transactional(readOnly = true)
    public ArticleEntity findById(@NonNull final Integer articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(ArticleEntity.class, articleId.toString()));
    }

    @NonNull
    @Transactional(readOnly = true)
    public ArticleEntity findByAuthorIdAndArticleId(@NonNull final String authorId, @NonNull final Integer articleId) {
        return articleRepository.findByIdAndAuthorIdAndEnabledIsTrue(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, ARTICLE_ID_FIELD_NAME, articleId.toString(), AUTHOR_ID_FIELD_NAME, authorId));
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<ArticleEntity> findAllEnabled(@NonNull final String userId, final Pageable pageable) {
        return articleRepository.findByAuthorIdAndEnabledIsTrue(userId, pageable);
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<ArticleEntity> findAllEnabled(final String titleContains, final String tagName, final Pageable pageable) {
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
    public Page<ArticleEntity> findBySubscriptions(@NonNull final String userId, final Pageable pageable) {
        return articleRepository.findBySubscriptions(userId, pageable);
    }

    @NonNull
    @Transactional
    public ArticleEntity create(@NonNull final String userId, @NonNull final String title, final List<String> tags,
                                @NonNull final String description, @NonNull final String body, @NonNull final StatusArticle status) {

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
    public ArticleEntity update(@NonNull final String authorId, @NonNull final Integer articleId, @NonNull final String title, final List<String> tags,
                                @NonNull final String description, @NonNull final String body, @NonNull final StatusArticle status) {
        final ArticleEntity articleEntity = findByAuthorIdAndArticleId(authorId, articleId);

        articleEntity.setTitle(title);
        articleEntity.setTags(tagService.createAndGetAllByName(tags));
        articleEntity.setDescription(description);
        articleEntity.setBody(body);

        if (status == PUBLISHED && articleEntity.getStatus() == CREATED) {
            articleEntity.setPublicationDate(LocalDateTime.now());
        }
        if (status == CREATED && articleEntity.getStatus() == PUBLISHED) {
            articleEntity.setPublicationDate(null);
        }
        articleEntity.setStatus(status);

        return articleRepository.save(articleEntity);
    }

    @Transactional
    public void delete(@NonNull final String authorId, @NonNull final Integer articleId) {
        final ArticleEntity articleEntity = articleRepository.findByIdAndAuthorId(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, ARTICLE_ID_FIELD_NAME, articleId.toString(), AUTHOR_ID_FIELD_NAME, authorId));
        articleRepository.delete(articleEntity);
    }

    @Transactional
    public void enable(@NonNull final String authorId, @NonNull final Integer articleId, @NonNull final Boolean enabled) {
        final ArticleEntity articleEntity = articleRepository.findByIdAndAuthorId(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, ARTICLE_ID_FIELD_NAME, articleId.toString(), AUTHOR_ID_FIELD_NAME, authorId));

        articleEntity.setEnabled(enabled);
        articleRepository.save(articleEntity);
    }
}
