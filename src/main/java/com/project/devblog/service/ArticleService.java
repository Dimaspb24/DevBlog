package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.SortingParam;
import com.project.devblog.model.enums.SortOrder;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.service.exception.ArticleConflictException;
import com.project.devblog.service.exception.ArticleNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class ArticleService {

    @NonNull
    private final ArticleRepository articleRepository;
    @NonNull
    private final UserService userService;

    @NonNull
    public ArticleEntity get(@NonNull Integer articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
    }

    @NonNull
    public ArticleEntity get(@NonNull Integer articleId, @NonNull Integer authorId) {
        return articleRepository.findByIdAndAuthorIdAndEnabledIsTrue(articleId, authorId).orElseThrow(ArticleNotFoundException::new);
    }

    @NonNull
    public ArticleEntity create(@NonNull Integer userId, @NonNull String title, List<TagEntity> tags,
                                @NonNull String description, @NonNull String body, @NonNull StatusArticle status) {

        final UserEntity author = userService.get(userId);
        final ArticleEntity articleEntity = new ArticleEntity(title, body, status, description, author);
        articleEntity.setTags(tags);

        return articleRepository.save(articleEntity);
    }

    @NonNull
    public Page<ArticleEntity> getAll(@NonNull Integer userId, @NonNull Pageable pageable) {
        return articleRepository.findByAuthorIdAndEnabledIsTrue(userId, pageable);
    }

    public void delete(@NonNull Integer authorId, @NonNull Integer articleId) {
        final ArticleEntity articleEntity = get(articleId, authorId);

        if (articleEntity.getEnabled()) {
            final LocalDateTime now = LocalDateTime.now();

            articleEntity.setEnabled(false);
            articleEntity.setDeletionDate(now);
            articleRepository.save(articleEntity);
        }

        throw new ArticleConflictException();
    }

    @NonNull
    public ArticleEntity update(@NonNull Integer authorId, @NonNull Integer articleId, @NonNull String title, List<TagEntity> tags,
                                @NonNull String description, @NonNull String body, @NonNull StatusArticle status) {
        final ArticleEntity articleEntity = get(articleId, authorId);

        articleEntity.setTitle(title);
        articleEntity.setTags(tags);
        articleEntity.setDescription(description);
        articleEntity.setBody(body);
        articleEntity.setStatus(status);

        return articleRepository.save(articleEntity);
    }

    public void publish(@NonNull Integer articleId) {
        final ArticleEntity article = get(articleId);
        article.setPublicationDate(LocalDateTime.now());
        article.setStatus(StatusArticle.PUBLISHED);
        articleRepository.save(article);
    }

    @NonNull
    public Page<ArticleEntity> getSortedList(@NonNull SortingParam sortingParam,
                                                 @NonNull SortOrder sortOrder, @NonNull Pageable pageable) {
        switch (sortingParam) {
            case PUBLICATION_DATE: {
                switch (sortOrder) {
                    case ASCENDING: {
                        return articleRepository.findByEnabledIsTrue(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                                Sort.by(sortingParam.getName()).ascending()));
                    }
                    case DESCENDING:
                        return articleRepository.findByEnabledIsTrue(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                                Sort.by(sortingParam.getName()).descending()));
                }
            }
            case RATING: {
                final Sort.Order orderByPublicationDateDesc =
                        new Sort.Order(Sort.Direction.DESC, SortingParam.PUBLICATION_DATE.getName());
                final Sort.Order orderByRating = Sort.Order.by(sortingParam.getName());

                switch (sortOrder) {
                    case ASCENDING:
                        orderByRating.isAscending();
                    case DESCENDING:
                        orderByRating.isDescending();
                }
                final List<Sort.Order> orders = new ArrayList<>(Arrays.asList(orderByRating, orderByPublicationDateDesc));

                return articleRepository.findByEnabledIsTrue(PageRequest
                        .of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders)));
            }
        }

        return articleRepository.findByEnabledIsTrue(pageable);
    }
}
