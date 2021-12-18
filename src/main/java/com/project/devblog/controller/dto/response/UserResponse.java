package com.project.devblog.controller.dto.response;

import com.project.devblog.model.PersonalInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class UserResponse {
    Integer id;
    String login;
    PersonalInfo personalInfo;
}
