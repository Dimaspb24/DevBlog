package com.project.devblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Jacksonized
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloseArticleResponse {

    @NonNull
    Integer id;
    @NonNull
    String title;
    @NonNull
    String status;
    @NonNull
    String description;
    @Nullable
    Double rating;
    @Nullable
    LocalDateTime publicationDate;
    @NonNull
    LocalDateTime modificationDate;
    @NonNull
    String authorId;
    @NonNull
    String nickname;
    @Nullable
    String photo;
    @Nullable
    List<TagResponse> tags;
}
