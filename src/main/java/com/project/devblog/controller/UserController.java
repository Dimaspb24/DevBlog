//package com.project.devblog.controller;
//
//import com.project.devblog.controller.annotation.ApiV1;
//import com.project.devblog.controller.dto.request.UserRequest;
//import com.project.devblog.controller.dto.response.UserResponse;
//import javax.validation.Valid;
//import lombok.AllArgsConstructor;
//import lombok.NonNull;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//@ApiV1
//@RestController
//public class UserController {
//
//    @PostMapping("/users")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponse create(@NonNull @Valid UserRequest request) {
//        return toResponse();
//    }
//
//    @GetMapping("/users/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public UserResponse get(@NonNull @PathVariable String userId) {
//        return toResponse();
//    }
//
//    @GetMapping("/users")
//    @ResponseStatus(HttpStatus.OK)
//    public Page<UserResponse> getAll(@NonNull Pageable pageable) {
//        return toResponse();
//    }
//
//    @DeleteMapping("/users/{userId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public UserResponse delete(@NonNull @PathVariable String userId) {
//        return toResponse();
//    }
//
//    @PutMapping("/users/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public UserResponse update(@NonNull @PathVariable String userId,
//                               @NonNull @Valid UserRequest userRequest) {
//        return toResponse();
//    }
//
//    @NonNull
//    private UserResponse toResponse(@NonNull UserEntity user) {
//        return new UserResponse();
//    }
//}
