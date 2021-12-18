package com.project.devblog.controller.dto.response;

import com.sun.istack.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class OpenArticleResponse {
    @NonNull
    private final Integer id;
    @NonNull
    private final String title;
    @NonNull
    private final String body;
    @NonNull
    private final String status;
    @NonNull
    private final String description;
    @Nullable
    private final LocalDateTime publicationDate;
    @NonNull
    private final LocalDateTime modificationDate;
    @NonNull
    private final Integer authorId;
    @Nullable
    private final List<TagResponse> tags;
}
