package com.project.devblog.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {

    @NotNull(message = "Rating is required")
    @Size(max = 10, message = "The rating range must be between 0 and 10")
    Integer rating;
}
