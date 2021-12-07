package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.ArticleRequest;
import com.project.devblog.controller.dto.response.CloseArticleResponse;
import com.project.devblog.controller.dto.response.OpenArticleResponse;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@AllArgsConstructor
public class UserArticleController {

    @PostMapping("/users/{userId}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public OpenArticleResponse create(@NonNull @PathVariable String userId, @NonNull @Valid ArticleRequest request) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public OpenArticleResponse get(@NonNull @PathVariable String userId, @NonNull @PathVariable String articleId) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/articles")
    @ResponseStatus(HttpStatus.OK)
    public Page<CloseArticleResponse> getAll(@NonNull @PathVariable String userId, @PageableDefault Pageable pageable) {
        return toResponse();
    }

    @DeleteMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public OpenArticleResponse delete(@NonNull @PathVariable String userId, @NonNull @PathVariable String articleId) {
        return toResponse();
    }

    @PutMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public OpenArticleResponse update(@NonNull @PathVariable String userId, @NonNull @PathVariable String articleId,
                                      @NonNull @Valid ArticleRequest request) {
        return toResponse();
    }

    @NonNull
    private OpenArticleResponse toResponse(@NonNull Article article) {
        return new OpenArticleResponse();
    }

    @NonNull
    private CloseArticleResponse toCloseArticleResponse(@NonNull Article article) {
        return new CloseArticleResponse();
    }
}
