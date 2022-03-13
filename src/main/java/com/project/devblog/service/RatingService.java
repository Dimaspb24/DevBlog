package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.UserArticleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RatingService {

    private final UserArticleRepository userArticleRepository;
    private final ArticleService articleService;
    private final UserService userService;

    @NonNull
    @Transactional
    public UserArticleEntity create(@NonNull String authorId, @NonNull Integer articleId, @NonNull Integer rating) {
        final ArticleEntity articleEntity = articleService.findByAuthorIdAndArticleId(authorId, articleId);
        final UserEntity userEntity = userService.findById(authorId);
        final UserArticleEntity userArticleEntity = new UserArticleEntity(rating, userEntity, articleEntity);

        return userArticleRepository.save(userArticleEntity);
    }

    @NonNull
    @Transactional(readOnly = true)
    public UserArticleEntity findByUserIdAndArticleId(@NonNull String userId, @NonNull Integer articleId) {
        return userArticleRepository.findByUserIdAndArticleIdAndArticleEnabledIsTrue(userId, articleId).orElseThrow(() ->
                new NotFoundException(UserArticleEntity.class, "userId", userId, "articleId", articleId.toString()));
    }

    @NonNull
    @Transactional
    public UserArticleEntity update(@NonNull String userId, @NonNull Integer articleId, @NonNull Integer rating) {
        final UserArticleEntity userArticleEntity = findByUserIdAndArticleId(userId, articleId);

        userArticleEntity.setRating(rating);

        return userArticleRepository.save(userArticleEntity);
    }
}
