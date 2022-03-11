package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.StatusArticle;
import static com.project.devblog.model.enums.StatusArticle.CREATED;
import static com.project.devblog.model.enums.StatusArticle.PUBLISHED;
import com.project.devblog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final TagService tagService;

    /*@NonNull*/
    public ArticleEntity find(/*@NonNull*/ String authorId, /*@NonNull*/ Integer articleId) {
        return articleRepository.findByIdAndAuthorIdAndEnabledIsTrue(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, "articleId", articleId.toString(), "authorId", authorId));
    }

    /*@NonNull*/
    public ArticleEntity findById(/*@NonNull*/ Integer articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(ArticleEntity.class, articleId.toString()));
    }

    /*@NonNull*/
    public ArticleEntity create(/*@NonNull*/ String userId, /*@NonNull*/ String title, List<String> tags,
            /*@NonNull*/ String description, /*@NonNull*/ String body, /*@NonNull*/ StatusArticle status) {

        final UserEntity author = userService.find(userId);
        final ArticleEntity articleEntity = new ArticleEntity(title, body, status, description, author);

        if (status.toString().equalsIgnoreCase(PUBLISHED.toString())) {
            articleEntity.setPublicationDate(LocalDateTime.now());
        }

        if (tags != null) {
            articleEntity.setTags(tagService.createAndGetAllByName(tags));
        }

        return articleRepository.save(articleEntity);
    }

    /*@NonNull*/
    public Page<ArticleEntity> findAll(/*@NonNull*/ String userId, Pageable pageable) {
        return articleRepository.findByAuthorIdAndEnabledIsTrue(userId, pageable);
    }

    public void enable(/*@NonNull*/ String authorId, /*@NonNull*/ Integer articleId, /*@NonNull*/ Boolean enabled) {
        final ArticleEntity articleEntity = articleRepository.findByIdAndAuthorId(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, "articleId", articleId.toString(), "authorId", authorId));

        articleEntity.setEnabled(enabled);
        articleRepository.save(articleEntity);
    }

    public void delete(/*@NonNull*/ String authorId, /*@NonNull*/ Integer articleId) {
        final ArticleEntity articleEntity = articleRepository.findByIdAndAuthorId(articleId, authorId).orElseThrow(() ->
                new NotFoundException(ArticleEntity.class, "articleId", articleId.toString(), "authorId", authorId));
        articleRepository.delete(articleEntity);
    }

    /*@NonNull*/
    public ArticleEntity update(/*@NonNull*/ String authorId, /*@NonNull*/ Integer articleId, /*@NonNull*/ String title, List<String> tags,
            /*@NonNull*/ String description, /*@NonNull*/ String body, /*@NonNull*/ StatusArticle status) {
        final ArticleEntity articleEntity = find(authorId, articleId);
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

    /*@NonNull*/
    public Page<ArticleEntity> findArticlesBySubscriptions(/*@NonNull*/ String userId, Pageable pageable) {
        return articleRepository.findBySubscriptions(userId, pageable);
    }

    /*@NonNull*/
    public Page<ArticleEntity> findAll(String titleContains, String tagName, Pageable pageable) {
        Page<ArticleEntity> articleEntities;
        if (Objects.isNull(titleContains) && Objects.isNull(tagName)) {
            articleEntities = articleRepository.findByEnabledIsTrueAndPublicationDateIsNotNull(pageable);
        } else if (Objects.nonNull(titleContains) && Objects.nonNull(tagName)) {
            PageRequest pageRequest = getPageRequestForSortByArticles(pageable);
            articleEntities = articleRepository.findByTagNameAndTitleContains(tagName, titleContains, pageRequest);
        } else if (Objects.nonNull(titleContains)) {
            articleEntities = articleRepository.findByEnabledIsTrueAndTitleContains(titleContains, pageable);
        } else {
            PageRequest pageRequest = getPageRequestForSortByArticles(pageable);
            articleEntities = articleRepository.findByTagName(tagName, pageRequest);
        }
        return articleEntities;
    }

    /*@NonNull*/
    private PageRequest getPageRequestForSortByArticles(Pageable pageable) {
        List<Sort.Order> orders = pageable.getSort().stream().map(order ->
                order.withProperty("a." + order.getProperty()))
                .collect(Collectors.toList());
        Sort sort = Sort.by(orders);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
