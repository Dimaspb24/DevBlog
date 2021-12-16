package com.project.devblog.controller.dto.response;

import com.project.devblog.model.TagEntity;
import com.project.devblog.model.enums.StatusArticle;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class CloseArticleResponse {
    @NonNull
    private final Integer id;
    @NonNull
    private final String title;
    @NonNull
    private final StatusArticle status;
    @NonNull
    private final String description;
    @NonNull
    private final LocalDateTime publicationDate;
    @NonNull
    private final LocalDateTime modificationDate;
    @NonNull
    private final Integer authorId;
    @NonNull
    private final List<TagEntity> tags;
}
