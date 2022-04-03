package com.project.devblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@EqualsAndHashCode(exclude = {"creationDate"})
@Getter
@Builder
@Jacksonized
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
