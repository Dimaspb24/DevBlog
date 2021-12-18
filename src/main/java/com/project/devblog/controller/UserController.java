package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.UserRequest;
import com.project.devblog.controller.dto.response.UserResponse;
import com.project.devblog.model.UserEntity;
import com.project.devblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ApiV1
@AllArgsConstructor
@RestController
public class UserController {

    @NonNull
    private final UserService userService;

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse get(@NonNull @PathVariable Integer userId) {
        UserEntity user = userService.findById(userId);
        return toResponse(user);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> getAll(@NonNull Pageable pageable) {
        return userService.findAll(pageable)
                .map(this::toResponse);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@NonNull @PathVariable Integer userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@NonNull @PathVariable Integer userId,
                               @RequestBody @NonNull @Valid UserRequest userRequest) {
        UserEntity user = userService.update(userId, userRequest);
        return toResponse(user);
    }

    @NonNull
    private UserResponse toResponse(@NonNull UserEntity user) {
        return new UserResponse(user.getId(), user.getLogin(), user.getPersonalInfo());
    }
}
