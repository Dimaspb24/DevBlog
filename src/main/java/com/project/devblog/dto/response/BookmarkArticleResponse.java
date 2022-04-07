package com.project.devblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

@Getter
@Builder
@Jacksonized
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkArticleResponse {

    @NonNull
    Long id;
    @Nullable
    Integer rating;
    @NonNull
    String bookmarkType;
    @NonNull
    CloseArticleResponse articleResponse;
}
