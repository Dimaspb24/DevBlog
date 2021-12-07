package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.CommentRequest;
import com.project.devblog.controller.dto.response.SubscribeResponse;
import javax.validation.Valid;
import lombok.NonNull;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ApiV1
@RestController
public class SubscribeController {

    @PostMapping("/users/{subscriberId}/subscribes/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscribeResponse create(@NonNull @PathVariable String subscriberId, @NonNull @PathVariable String userId) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/subscribes/{subscribeId}")
    @ResponseStatus(HttpStatus.OK)
    public SubscribeResponse get(@NonNull @PathVariable String userId, @NonNull @PathVariable String subscribeId) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/subscribes")
    @ResponseStatus(HttpStatus.OK)
    public Page<SubscribeResponse> getAll(@NonNull @PathVariable String userId, @NonNull Pageable pageable) {
        return toResponse();
    }

    @DeleteMapping("/users/{userId}/subscribes/{subscriberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SubscribeResponse delete(@NonNull @PathVariable String userId, @NonNull @PathVariable String subscriberId) {
        return toResponse();
    }

    @NonNull
    private SubscribeResponse toResponse(@NonNull User comment) {
        return new SubscribeResponse();
    }
}
