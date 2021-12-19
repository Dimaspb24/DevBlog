package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.service.exception.CommentNotFoundException;
import com.sun.istack.Nullable;
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
    private final AuthUserService userService;

    @NonNull
    public UserArticleEntity create(@NonNull Integer authorId, @NonNull Integer articleId, @NonNull Integer rating) {
        final ArticleEntity articleEntity = articleService.get(articleId, authorId);
        final UserEntity userEntity = userService.get(authorId);
        final UserArticleEntity userArticleEntity = new UserArticleEntity(rating, userEntity, articleEntity);

        return userArticleRepository.save(userArticleEntity);
    }

    @Nullable
    public UserArticleEntity get(@NonNull Integer userId, @NonNull Integer articleId) {
        return userArticleRepository.findByUserIdAndArticleIdAndArticleEnabledIsTrue(userId, articleId).orElse(null);
    }

    @NonNull
    public UserArticleEntity update(@NonNull Integer userId, @NonNull Integer articleId, @NonNull Integer rating) {
        final UserArticleEntity userArticleEntity = userArticleRepository.findByUserIdAndArticleIdAndArticleEnabledIsTrue(userId, articleId)
                .orElseThrow(CommentNotFoundException::new);
        userArticleEntity.setRating(rating);

        return userArticleRepository.save(userArticleEntity);
    }
}
