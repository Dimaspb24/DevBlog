package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.CommentRepository;
import com.project.devblog.service.exception.CommentConflictException;
import com.project.devblog.service.exception.CommentNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    @NonNull
    private final CommentRepository commentRepository;
    @NonNull
    private final ArticleService articleService;
    @NonNull
    private final UserService userService;

    @NonNull
    public CommentEntity create(@NonNull String authorCommentId, @NonNull Integer articleId, @NonNull String message,
                                String receiverId) {

        final ArticleEntity articleEntity = articleService.get(authorCommentId, articleId);
        final UserEntity author = userService.get(authorCommentId);
        final CommentEntity commentEntity = new CommentEntity(message, articleEntity, author);
        final UserEntity receiver = (receiverId == null) ? articleEntity.getAuthor() : userService.get(receiverId);
        commentEntity.setReceiver(receiver);

        return commentRepository.save(commentEntity);
    }

    @NonNull
    public CommentEntity get(@NonNull Long id, @NonNull String authorId, @NonNull Integer articleId) {
        return commentRepository.findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(id, authorId, articleId).orElseThrow(CommentNotFoundException::new);
    }

    @NonNull
    public Page<CommentEntity> getAllByArticleId(@NonNull Integer articleId, @NonNull String authorId, @NonNull Pageable pageable) {
        return commentRepository.findAllByArticleIdAndEnabledIsTrue(articleId, pageable);
    }

    public void enable(@NonNull Long id, @NonNull String authorId, @NonNull Integer articleId, @NonNull Boolean enabled) {
        final CommentEntity commentEntity = commentRepository.findByIdAndAuthorIdAndArticleId(id, authorId, articleId)
                .orElseThrow(CommentNotFoundException::new);

        if (enabled.equals(commentEntity.getEnabled())) {
            String message = enabled ? "Comment is already enabled" : "Comment is already disabled";
            throw new CommentConflictException(message);
        }

        commentEntity.setEnabled(enabled);
        commentRepository.save(commentEntity);
    }

    public void delete(@NonNull Long id, @NonNull String authorId, @NonNull Integer articleId) {
        final CommentEntity commentEntity = commentRepository.findByIdAndAuthorIdAndArticleId(id, authorId, articleId)
                .orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(commentEntity);
    }

    @NonNull
    public CommentEntity update(@NonNull Long id, @NonNull String message, @NonNull Integer articleId, @NonNull String authorId) {
        final CommentEntity commentEntity = get(id, authorId, articleId);
        commentEntity.setMessage(message);

        return commentRepository.save(commentEntity);
    }
}
