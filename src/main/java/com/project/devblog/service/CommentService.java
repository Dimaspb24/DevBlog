package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.CommentRepository;
import com.project.devblog.service.exception.CommentNotFoundException;
import com.project.devblog.service.idgenerator.Generator;
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
    private final Generator idGenerator;

    @NonNull
    public CommentEntity create(@NonNull String authorCommentId, @NonNull String articleId, @NonNull String message,
                                String receiverId) {

        final ArticleEntity articleEntity = articleService.get(authorCommentId, articleId);
        final UserEntity author = userService.get(authorCommentId);
        final CommentEntity commentEntity = new CommentEntity(idGenerator.generateId(), message, articleEntity, author);
        final UserEntity receiver = (receiverId == null) ? articleEntity.getAuthor() : userService.get(receiverId);
        commentEntity.setReceiver(receiver);

        return commentRepository.save(commentEntity);
    }

    @NonNull
    public CommentEntity get(@NonNull String id, @NonNull String authorId, @NonNull String articleId) {
        return commentRepository.findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(id, authorId, articleId).orElseThrow(CommentNotFoundException::new);
    }

    @NonNull
    public Page<CommentEntity> getAllByArticleId(@NonNull String articleId, @NonNull String authorId, @NonNull Pageable pageable) {
        return commentRepository.findAllByArticleIdAndAuthorIdAndEnabledIsTrue(articleId, authorId, pageable);
    }

    public void deleteComment(@NonNull String id, @NonNull String authorId, @NonNull String articleId) {
        final ArticleEntity articleEntity = articleService.get(articleId, authorId);
        final CommentEntity commentEntity = get(id, authorId, articleId);
        articleEntity.getComments().remove(commentEntity);

        commentEntity.setEnabled(false);
        commentRepository.save(commentEntity);
    }

    @NonNull
    public CommentEntity update(@NonNull String id, @NonNull String message, @NonNull String articleId, @NonNull String authorId) {
        final CommentEntity commentEntity = get(id, authorId, articleId);
        commentEntity.setMessage(message);

        return commentRepository.save(commentEntity);
    }
}
