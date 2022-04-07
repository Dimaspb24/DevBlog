package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.dto.request.RatingRequest;
import com.project.devblog.dto.response.RatingResponse;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.service.RatingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User rating of articles")
@ApiV1
@RestController
@RequiredArgsConstructor
public class UserArticleRatingController {

    private final RatingService ratingService;

    @PostMapping("/users/{userId}/articles/{articleId}/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse createOrUpdate(@NonNull @PathVariable String userId,
                                         @NonNull @PathVariable Integer articleId,
                                         @NonNull @Valid @RequestBody RatingRequest request) {
        return toResponse(ratingService.createOrUpdate(userId, articleId, request.getRating()));
    }

    @GetMapping("/users/{userId}/articles/{articleId}/ratings")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse find(@NonNull @PathVariable String userId,
                               @NonNull @PathVariable Integer articleId) {
        return toResponse(ratingService.findByUserIdAndArticleId(userId, articleId));
    }

    @NonNull
    private RatingResponse toResponse(@NonNull UserArticleEntity userArticleEntity) {
        return new RatingResponse(userArticleEntity.getUser().getId(),
                userArticleEntity.getArticle().getId(),
                userArticleEntity.getRating());
    }
}
