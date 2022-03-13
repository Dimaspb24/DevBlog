package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.CommentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;

    @NonNull
    @Transactional
    public CommentEntity create(@NonNull String authorCommentId, @NonNull Integer articleId, @NonNull String message,
                                String receiverId) {

        final ArticleEntity articleEntity = articleService.findByAuthorIdAndArticleId(authorCommentId, articleId);
        final UserEntity author = userService.findById(authorCommentId);
        final CommentEntity commentEntity = new CommentEntity(message, articleEntity, author);
        final UserEntity receiver = (receiverId == null) ? articleEntity.getAuthor() : userService.findById(receiverId);
        commentEntity.setReceiver(receiver);

        return commentRepository.save(commentEntity);
    }

    @NonNull
    @Transactional(readOnly = true)
    public CommentEntity findByIdAndAuthorIdAndArticleId(@NonNull Long id, @NonNull String authorId, @NonNull Integer articleId) {
        return commentRepository.findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(id, authorId, articleId).orElseThrow(() ->
                new NotFoundException(CommentEntity.class, id.toString()));
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<CommentEntity> findAllByAuthorIdAndArticleId(@NonNull String authorId, @NonNull Integer articleId, Pageable pageable) {
        return commentRepository.findAllByAuthorIdAndArticleIdAndEnabledIsTrue(authorId, articleId, pageable);
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<CommentEntity> findAllByArticleId(@NonNull Integer articleId, Pageable pageable) {
        return commentRepository.findAllByArticleIdAndEnabledIsTrue(articleId, pageable);
    }

    @NonNull
    @Transactional
    public CommentEntity update(@NonNull Long id, @NonNull String message, @NonNull Integer articleId, @NonNull String authorId) {
        final CommentEntity commentEntity = findByIdAndAuthorIdAndArticleId(id, authorId, articleId);
        commentEntity.setMessage(message);

        return commentRepository.save(commentEntity);
    }

    @Transactional
    public void enable(@NonNull Long id, @NonNull String authorId, @NonNull Integer articleId, @NonNull Boolean enabled) {
        final CommentEntity commentEntity = commentRepository.findByIdAndAuthorIdAndArticleId(id, authorId, articleId)
                .orElseThrow(() -> new NotFoundException(CommentEntity.class, id.toString()));

        commentEntity.setEnabled(enabled);
        commentRepository.save(commentEntity);
    }

    @Transactional
    public void delete(@NonNull Long id, @NonNull String authorId, @NonNull Integer articleId) {
        final CommentEntity commentEntity = commentRepository.findByIdAndAuthorIdAndArticleId(id, authorId, articleId)
                .orElseThrow(() -> new NotFoundException(CommentEntity.class, id.toString()));
        commentRepository.delete(commentEntity);
    }
}
