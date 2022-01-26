package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.service.exception.CommentNotFoundException;
import com.project.devblog.service.exception.RatingNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@AllArgsConstructor
public class RatingService {

    @NonNull
    private final UserArticleRepository userArticleRepository;
    @NonNull
    private final ArticleService articleService;
    @NonNull
    private final UserService userService;

    @NonNull
    public UserArticleEntity create(@NonNull String authorId, @NonNull Integer articleId, @NonNull Integer rating) {
        final ArticleEntity articleEntity = articleService.get(authorId, articleId);
        final UserEntity userEntity = userService.get(authorId);
        final UserArticleEntity userArticleEntity = new UserArticleEntity(rating, userEntity, articleEntity);

        return userArticleRepository.save(userArticleEntity);
    }

    @NonNull
    public UserArticleEntity get(@NonNull String userId, @NonNull Integer articleId) {
        return userArticleRepository.findByUserIdAndArticleIdAndArticleEnabledIsTrue(userId, articleId).orElseThrow(RatingNotFoundException::new);
    }

    @NonNull
    public UserArticleEntity update(@NonNull String userId, @NonNull Integer articleId, @NonNull Integer rating) {
        final UserArticleEntity userArticleEntity = userArticleRepository.findByUserIdAndArticleIdAndArticleEnabledIsTrue(userId, articleId)
                .orElseThrow(CommentNotFoundException::new);
        userArticleEntity.setRating(rating);

        return userArticleRepository.save(userArticleEntity);
    }
}
