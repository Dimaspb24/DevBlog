package com.project.devblog.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingRequest {
    @NotNull(message = "Rating is required")
    private final Integer rating;
}
