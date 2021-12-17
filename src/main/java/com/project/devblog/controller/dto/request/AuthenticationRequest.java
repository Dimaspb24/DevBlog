package com.project.devblog.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class AuthenticationRequest {

    @NotBlank(message = "Login is required")
    private final String login;
    @NotBlank(message = "Password is required")
    private String password;
}