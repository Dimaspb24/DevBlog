package com.project.devblog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationRequest {

    @Email(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    @Schema(example = "mail1@mail.ru")
    String login;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 45, message = "The password must be from 8 to 45 characters")
    @Schema(example = "password")
    String password;
}