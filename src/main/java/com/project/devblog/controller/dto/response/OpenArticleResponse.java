package com.project.devblog.controller.dto.response;

import com.project.devblog.model.CommentEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserArticleEntity;
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
    @NonNull
    private final LocalDateTime publicationDate;
    @NonNull
    private final LocalDateTime modificationDate;
    @NonNull
    private final Integer authorId;
    @NonNull
    private final List<CommentEntity> comments;
    @NonNull
    private final List<TagEntity> tags;
    @NonNull
    private final List<UserArticleEntity> relationUsers;
}
