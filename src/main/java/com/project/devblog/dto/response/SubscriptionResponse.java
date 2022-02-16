package com.project.devblog.dto.response;

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
    String authorId;
    @Nullable
    String firstname;
    @Nullable
    String lastname;
    @NonNull
    String nickname;
}
