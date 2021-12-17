package com.project.devblog.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class RegistrationResponse {

    @NonNull
    private final Integer id;
    @NonNull
    private final String login;
}
