package com.project.devblog.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class CommentResponse {
    @NonNull
    private final Integer id;
    @NonNull
    private final String message;
    @NonNull
    private final Integer authorId;
    @NonNull
    private final Integer receiverId;
    @NonNull
    private final Integer articleId;
}
