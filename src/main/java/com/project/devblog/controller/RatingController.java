package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.RatingRequest;
import com.project.devblog.controller.dto.response.RatingResponse;
import javax.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ApiV1
@RestController
public class RatingController {

    @PostMapping("/users/{userId}/articles/{articleId}/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse create(@NonNull @PathVariable String userId, @NonNull @Valid RatingRequest request) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/articles/{articleId}/ratings")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse get(@NonNull @PathVariable String userId) {
        return toResponse();
    }

    @PutMapping("/users/{userId}/articles/{articlesId}/ratings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public RatingResponse delete(@NonNull @PathVariable String userId, @NonNull @Valid RatingRequest request) {
        return toResponse();
    }

    @NonNull
    private RatingResponse toResponse(@NonNull Article article) {
        return new RatingResponse();
    }
}
