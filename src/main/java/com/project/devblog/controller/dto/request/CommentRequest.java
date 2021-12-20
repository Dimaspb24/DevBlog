package com.project.devblog.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Message is required")
    private final String message;
    @Nullable
    private final Integer receiverId;
}
