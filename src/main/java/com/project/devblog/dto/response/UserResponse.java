package com.project.devblog.dto.response;

import com.project.devblog.model.PersonalInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResponse {

    @NonNull
    String id;
    @NonNull
    String login;
    @NonNull
    PersonalInfo personalInfo;
}
