package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.response.SubscriptionResponse;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Subscription")
@ApiV1
@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/users/{userId}/subscriptions/{authorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> subscribe(@NonNull @PathVariable String userId, @NonNull @PathVariable String authorId) {
        subscriptionService.subscribe(userId, authorId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{userId}/subscriptions/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@NonNull @PathVariable String userId, @NonNull @PathVariable String authorId) {
        subscriptionService.unsubscribe(userId, authorId);
    }

    @GetMapping("/users/{userId}/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public Page<SubscriptionResponse> getSubscriptions(@NonNull @PathVariable String userId, @NonNull Pageable pageable) {
        return subscriptionService.findSubscriptions(userId, pageable)
                .map(this::toResponse);
    }

    @GetMapping("/users/{userId}/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public Page<SubscriptionResponse> getSubscribers(@NonNull @PathVariable String userId, @NonNull Pageable pageable) {
        return subscriptionService.findSubscribers(userId, pageable)
                .map(this::toResponse);
    }

    @NonNull
    private SubscriptionResponse toResponse(@NonNull UserEntity user) {
        PersonalInfo personalInfo = user.getPersonalInfo();
        return new SubscriptionResponse(user.getId(), personalInfo.getFirstname(),
                personalInfo.getLastname(), personalInfo.getNickname());
    }
}
