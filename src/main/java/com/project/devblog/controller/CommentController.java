package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.ArticleRequest;
import com.project.devblog.controller.dto.request.CommentRequest;
import com.project.devblog.controller.dto.response.CommentResponse;
import javax.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ApiV1
@RestController
public class CommentController {

    @PostMapping("/users/{userId}/articles/{articleId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@NonNull @PathVariable String userId, @NonNull @PathVariable String articleId,
                                  @NonNull @Valid CommentRequest request) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/articles/{articleId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse get(@NonNull @PathVariable String userId, @NonNull @PathVariable String articleId,
                               @NonNull @PathVariable String commentId) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/articles/{articleId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentResponse> getAll(@NonNull @PathVariable String userId, @NonNull @PathVariable String articleId,
                                        @NonNull Pageable pageable) {
        return toResponse();
    }

    @DeleteMapping("/users/{userId}/articles/{articleId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommentResponse delete(@NonNull @PathVariable String userId, @NonNull @PathVariable String articleId,
                                  @NonNull @PathVariable String commentId) {
        return toResponse();
    }

    @PutMapping("/users/{userId}/articles/{articleId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse update(@NonNull @PathVariable String userId, @NonNull @PathVariable String commentId,
                                  @NonNull @PathVariable String articleId, @NonNull @Valid ArticleRequest request) {
        return toResponse();
    }

    @NonNull
    private CommentResponse toResponse(@NonNull Comment comment) {
        return new CommentResponse();
    }
}
