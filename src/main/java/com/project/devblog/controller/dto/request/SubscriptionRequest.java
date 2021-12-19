package com.project.devblog.controller.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionRequest {
    @NotNull(message = "User id is required")
    Integer userId;
    @NotNull(message = "Author id is required")
    Integer authorId;
}
