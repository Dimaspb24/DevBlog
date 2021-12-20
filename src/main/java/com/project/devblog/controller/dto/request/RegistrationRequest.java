package com.project.devblog.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationRequest {

    @NotBlank(message = "Login is required")
    String login;
    @NotBlank(message = "Password is required")
    String password;
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "(USER)|(ADMIN)")
    String role;
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "(ACTIVE)|(BANNED)")
    String status;
}
