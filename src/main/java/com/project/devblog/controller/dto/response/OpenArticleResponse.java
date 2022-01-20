package com.project.devblog.controller.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OpenArticleResponse {
    @NonNull
    Integer id;
    @NonNull
    String title;
    @NonNull
    String body;
    @NonNull
    String status;
    @NonNull
    String description;
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
