package com.project.devblog.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationRequest {

    @Email(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    @NotBlank(message = "Login is required")
    String login;
    @NotBlank(message = "Password is required")
    String password;
}