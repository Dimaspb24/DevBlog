package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.CommentRequest;
import com.project.devblog.controller.dto.response.CommentResponse;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ApiV1
@RestController
@AllArgsConstructor
public class CommentController {

    @NonNull
    private final CommentService commentService;

    @PostMapping("/users/{userId}/articles/{articleId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@NonNull @PathVariable Integer userId, @NonNull @PathVariable Integer articleId,
                                  @NonNull @Valid CommentRequest request) {
        return toResponse(commentService.create(userId, articleId, request.getMessage(), request.getReceiverId()));
    }

    @GetMapping("/users/{userId}/articles/{articleId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse get(@NonNull @PathVariable Integer userId, @NonNull @PathVariable Integer articleId,
                               @NonNull @PathVariable Integer commentId) {
        return toResponse(commentService.get(commentId, userId, articleId));
    }

    @GetMapping("/users/{userId}/articles/{articleId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentResponse> getAll(@NonNull @PathVariable Integer userId, @NonNull @PathVariable Integer articleId,
                                        @NonNull Pageable pageable) {
        return commentService.getAllByArticleId(articleId, userId, pageable)
                .map(this::toResponse);
    }

    @DeleteMapping("/users/{userId}/articles/{articleId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable Integer userId, @NonNull @PathVariable Integer articleId,
                       @NonNull @PathVariable Integer commentId) {
        commentService.deleteComment(commentId, userId, articleId);
    }

    @PutMapping("/users/{userId}/articles/{articleId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse update(@NonNull @PathVariable Integer userId, @NonNull @PathVariable Integer commentId,
                                  @NonNull @PathVariable Integer articleId, @NonNull @Valid CommentRequest request) {
        return toResponse(commentService.update(commentId, request.getMessage(), articleId, userId));
    }

    @NonNull
    private CommentResponse toResponse(@NonNull CommentEntity comment) {
        return new CommentResponse(comment.getId(), comment.getMessage(), comment.getAuthor().getId(),
                comment.getReceiver().getId(), comment.getArticle().getId());
    }
}
