package com.project.devblog.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@AllArgsConstructor
public class BookmarkArticleResponse {
    @NonNull
    private final Long id;
    @NonNull
    private final CloseArticleResponse articleResponse;
}
