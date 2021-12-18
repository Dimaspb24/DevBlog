package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.CommentRepository;
import com.project.devblog.service.exception.CommentNotFoundException;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public CommentEntity create(@NonNull Integer authorId, @NonNull Integer articleId, @NonNull String message,
                                @NonNull Integer receiverId) {

        final ArticleEntity articleEntity = articleService.get(authorId, articleId);
        final UserEntity author = userService.get(authorId);
        final CommentEntity commentEntity = new CommentEntity(message, articleEntity, author);

        if (!receiverId.equals(authorId)) {
            final UserEntity receiver = userService.get(receiverId);
            commentEntity.setReceiver(receiver);
        }

        return commentRepository.save(commentEntity);
    }

    @NonNull
    public CommentEntity get(@NonNull Integer id, @NonNull Integer authorId, @NonNull Integer articleId) {
        return commentRepository.findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(id, authorId, articleId).orElseThrow(CommentNotFoundException::new);
    }

    @NonNull
    public Page<CommentEntity> getAllByArticleId(@NonNull Integer articleId, @NonNull Integer authorId, @NonNull Pageable pageable) {
        return commentRepository.findAllByArticleIdAndAuthorIdAndEnabledIsTrue(articleId, authorId, pageable);
    }

    public void deleteComment(@NonNull Integer id, @NonNull Integer authorId, @NonNull Integer articleId) {
        final ArticleEntity articleEntity = articleService.get(articleId, authorId);
        final CommentEntity commentEntity = get(id, authorId, articleId);
        articleEntity.getComments().remove(commentEntity);

        commentEntity.setEnabled(false);
        commentRepository.save(commentEntity);
    }

    @NonNull
    public CommentEntity update(@NonNull Integer id, @NonNull String message, @NonNull Integer articleId, @NonNull Integer authorId) {
        final CommentEntity commentEntity = get(id, authorId, articleId);
        commentEntity.setMessage(message);

        return commentRepository.save(commentEntity);
    }
}