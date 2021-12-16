package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.UserArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserArticleRepository userArticleRepository;

    public List<ArticleEntity> getAll() {
        return articleRepository.findAll();
    }

    public ArticleEntity save(ArticleEntity article) {
        if (articleRepository.findById(article.getId()).isPresent()) {
            article.setModificationDate(LocalDateTime.now());
        } else {
            article.setCreationDate(LocalDateTime.now());
            article.setStatus(StatusArticle.CREATED);
        }
        return articleRepository.save(article);
    }

    public void publish(ArticleEntity article) {
        article.setPublicationDate(LocalDateTime.now());
        article.setStatus(StatusArticle.PUBLISHED);
    }

    public Float getTotalRating(Integer articleId) {
        List<UserArticleEntity> userArticleEntityList = userArticleRepository.findAll();

        float totalRating = 0.0f;
        int countUsers = 0;
        for (UserArticleEntity userArticle : userArticleEntityList) {
            if (userArticle.getArticle().getId().equals(articleId) && userArticle.getRating() != null) {
                totalRating += userArticle.getRating();
                countUsers++;
            }
        }

        return totalRating /= countUsers;
    }

    public List<ArticleEntity> getSortedListByPublicationDateDescending() {
        List<ArticleEntity> sortedList = new ArrayList<>(articleRepository.findAll());
        Collections.sort(sortedList, Comparator.comparing(ArticleEntity::getPublicationDate).reversed());
        return sortedList;
    }

    public List<ArticleEntity> getSortedListByPublicationDateAscending() {
        List<ArticleEntity> sortedList = new ArrayList<>(articleRepository.findAll());
        Collections.sort(sortedList, Comparator.comparing(ArticleEntity::getPublicationDate));
        return sortedList;
    }

    public List<ArticleEntity> getSortedListByRatingDescending() {
        List<ArticleEntity> sortedList = new ArrayList<>(articleRepository.findAll());
        Collections.sort(sortedList, Comparator.comparing(article -> getTotalRating(article.getId())));
        Collections.reverse(sortedList);
        return sortedList;
    }

    public List<ArticleEntity> getSortedListByRatingAscending() {
        List<ArticleEntity> sortedList = new ArrayList<>(articleRepository.findAll());
        Collections.sort(sortedList, Comparator.comparing(article -> getTotalRating(article.getId())));
        return sortedList;
    }
}
