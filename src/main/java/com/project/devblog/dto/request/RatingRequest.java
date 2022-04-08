package com.project.devblog.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {

    @NotNull(message = "Rating is required")

    @Max(value = 10, message = "The rating range must less then or equal to 10")
    @Min(value = 0, message = "The rating range must greater than or equal to 0")
    Integer rating;
}
