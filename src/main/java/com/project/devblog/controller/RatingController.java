package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.RatingRequest;
import com.project.devblog.controller.dto.response.RatingResponse;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.service.RatingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Rating")
@ApiV1
@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/users/{userId}/articles/{articleId}/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse create(@NonNull @PathVariable String userId, @NonNull @PathVariable Integer articleId,
                                 @NonNull @Valid RatingRequest request) {
        return toResponse(ratingService.create(userId, articleId, request.getRating()));
    }

    @GetMapping("/users/{userId}/articles/{articleId}/ratings")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse get(@NonNull @PathVariable String userId, @NonNull @PathVariable Integer articleId) {
        return toResponse(ratingService.get(userId, articleId));
    }

    @PutMapping("/users/{userId}/articles/{articleId}/ratings")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse update(@NonNull @PathVariable String userId, @NonNull @PathVariable Integer articleId,
                                 @NonNull @Valid RatingRequest request) {
        return toResponse(ratingService.update(userId, articleId, request.getRating()));
    }

    @NonNull
    private RatingResponse toResponse(@NonNull UserArticleEntity userArticleEntity) {
        return new RatingResponse(userArticleEntity.getUser().getId(), userArticleEntity.getArticle().getId(),
                userArticleEntity.getRating());
    }
}
