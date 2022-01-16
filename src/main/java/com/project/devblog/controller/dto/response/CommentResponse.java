package com.project.devblog.controller.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentResponse {
    @NonNull
    String id;
    @NonNull
    String message;
    @NonNull
    String authorId;
    @NonNull
    String receiverId;
    @NonNull
    String articleId;
}
