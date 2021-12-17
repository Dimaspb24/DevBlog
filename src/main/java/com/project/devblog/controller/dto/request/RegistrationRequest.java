package com.project.devblog.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Getter
public class RegistrationRequest {

    @NotBlank(message = "Login is required")
    private final String login;
    @NotBlank(message = "Password is required")
    private final String password;
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "(USER)|(ADMIN)")
    private final String role;
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "(ACTIVE)|(BANNED)")
    private final String status;
}
