package com.project.devblog.controller.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionResponse {
    @NonNull
    String id;
    @Nullable
    String firstname;
    @Nullable
    String lastname;
    @NonNull
    String nickname;
}
