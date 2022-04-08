package com.project.devblog.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationRequest {

    @Email(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    String login;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 45, message = "The password must be from 8 to 45 characters")
    String password;
}
