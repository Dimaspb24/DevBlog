package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.response.SubscriptionResponse;
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
public class SubscriptionController {

    @PostMapping("/users/{subscriberId}/subscription/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse create(@NonNull @PathVariable String subscriberId, @NonNull @PathVariable String userId) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/subscription/{subscribeId}")
    @ResponseStatus(HttpStatus.OK)
    public SubscriptionResponse get(@NonNull @PathVariable String userId, @NonNull @PathVariable String subscribeId) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/subscription")
    @ResponseStatus(HttpStatus.OK)
    public Page<SubscriptionResponse> getAll(@NonNull @PathVariable String userId, @NonNull Pageable pageable) {
        return toResponse();
    }

    @DeleteMapping("/users/{userId}/subscription/{subscriberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SubscriptionResponse delete(@NonNull @PathVariable String userId, @NonNull @PathVariable String subscriberId) {
        return toResponse();
    }

    @NonNull
    private SubscriptionResponse toResponse(@NonNull User comment) {
        return new SubscriptionResponse();
    }
}
