package com.project.devblog.controller.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentResponse {

    @NonNull
    Long id;
    @NonNull
    String message;
    @NonNull
    String authorNickname;
    @NonNull
    String receiverNickname;
    @NonNull
    LocalDateTime creationDate;
    @NonNull
    Integer articleId;
}
