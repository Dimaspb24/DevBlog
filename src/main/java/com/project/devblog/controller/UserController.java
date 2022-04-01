package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.dto.request.UserRequest;
import com.project.devblog.dto.response.UserResponse;
import com.project.devblog.model.UserEntity;
import com.project.devblog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User")
@ApiV1
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse find(@NonNull @PathVariable String userId) {
        return toResponse(userService.findById(userId));
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> findAll(@ParameterObject Pageable pageable) {
        return userService.findAll(pageable)
                .map(this::toResponse);
    }

    @Operation(summary = "Block the user")
    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable String userId) {
        userService.delete(userId);
    }

    @Operation(summary = "Block or unblock the user")
    @PatchMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void enable(@NonNull @PathVariable String userId,
                       @NonNull @Valid @RequestParam Boolean enabled) {
        userService.enable(userId, enabled);
    }

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@NonNull @PathVariable String userId,
                               @NonNull @RequestBody UserRequest userRequest) {
        return toResponse(userService.update(userId, userRequest));
    }

    @NonNull
    private UserResponse toResponse(@NonNull UserEntity user) {
        return new UserResponse(user.getId(), user.getLogin(), user.getPersonalInfo());
    }
}
