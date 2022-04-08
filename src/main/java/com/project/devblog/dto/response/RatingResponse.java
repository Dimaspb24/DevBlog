package com.project.devblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingResponse {

    @NonNull
    String authorId;
    @NonNull
    Integer articleId;
    @NonNull
    Integer rating;
}
