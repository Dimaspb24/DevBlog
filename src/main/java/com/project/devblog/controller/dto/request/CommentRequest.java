package com.project.devblog.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Message is required")
    private final String message;
    @NotBlank(message = "Receiver id is required")
    private final Integer receiverId;
}
