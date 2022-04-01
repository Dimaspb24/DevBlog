package com.project.devblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
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
