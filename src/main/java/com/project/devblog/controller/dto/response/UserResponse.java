package com.project.devblog.controller.dto.response;

import com.project.devblog.model.PersonalInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Getter
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
