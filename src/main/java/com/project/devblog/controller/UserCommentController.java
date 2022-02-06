package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.CommentRequest;
import com.project.devblog.controller.dto.response.CommentResponse;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User comments")
@ApiV1
@RestController
@RequiredArgsConstructor
public class UserCommentController {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/articles/{articleId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@NonNull @PathVariable String userId,
                                  @NonNull @PathVariable Integer articleId,
                                  @NonNull @Valid CommentRequest request) {
        return toResponse(commentService.create(userId, articleId, request.getMessage(), request.getReceiverId()));
    }

    @GetMapping("/users/{userId}/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse find(@NonNull @PathVariable String userId,
                                @NonNull @PathVariable Integer articleId,
                                @NonNull @PathVariable Long commentId) {
        return toResponse(commentService.find(commentId, userId, articleId));
    }

    @GetMapping("/users/{userId}/articles/{articleId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentResponse> findAll(@NonNull @PathVariable String userId,
                                         @NonNull @PathVariable Integer articleId,
                                         @NonNull Pageable pageable) {
        return commentService.findAllByAuthorIdAndArticleId(userId, articleId, pageable)
                .map(this::toResponse);
    }

    @DeleteMapping("/users/{userId}/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable String userId,
                       @NonNull @PathVariable Integer articleId,
                       @NonNull @PathVariable Long commentId) {
        commentService.delete(commentId, userId, articleId);
    }

    @Operation(summary = "Hide or show the comment")
    @PatchMapping("/users/{userId}/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void enable(@NonNull @PathVariable String userId,
                       @NonNull @PathVariable Integer articleId,
                       @NonNull @PathVariable Long commentId,
                       @NonNull @Valid @RequestParam Boolean enabled) {
        commentService.enable(commentId, userId, articleId, enabled);
    }

    @PutMapping("/users/{userId}/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse update(@NonNull @PathVariable String userId,
                                  @NonNull @PathVariable Integer articleId,
                                  @NonNull @PathVariable Long commentId,
                                  @NonNull @Valid CommentRequest request) {
        return toResponse(commentService.update(commentId, request.getMessage(), articleId, userId));
    }

    @NonNull
    private CommentResponse toResponse(@NonNull CommentEntity comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getMessage(),
                comment.getAuthor().getId(),
                comment.getReceiver().getId(),
                comment.getArticle().getId());
    }
}
