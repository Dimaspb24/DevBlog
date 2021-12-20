package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.RegistrationRequest;
import com.project.devblog.controller.dto.response.RegistrationResponse;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@ApiV1
@RestController
@AllArgsConstructor
public class RegistrationController {

    @NonNull
    private final UserService userService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.OK)
    public RegistrationResponse registration(@NonNull @Valid RegistrationRequest request) {
        if (!userService.isExists(request.getLogin())) {
            return toResponse(userService.register(
                    request.getLogin(),
                    request.getPassword(),
                    Role.valueOf(request.getRole()),
                    StatusUser.valueOf(request.getStatus())));
        } else {
            throw new BadCredentialsException("Login already exists");
        }
    }

    @NonNull
    private RegistrationResponse toResponse(@NonNull UserEntity user) {
        return new RegistrationResponse(user.getId(), user.getLogin());
    }
}
