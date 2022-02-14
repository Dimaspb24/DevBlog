package com.project.devblog.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingRequest {

    @NotNull(message = "Rating is required")
    @Size(max = 10, message = "The rating range must be between 0 and 10")
    Integer rating;
}
