package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.SubscriberResponse;
import com.project.devblog.dto.response.SubscriptionResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.service.ArticleService;
import com.project.devblog.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "User subscription")
@ApiV1
@RestController
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final ArticleService articleService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/users/{userId}/subscriptions/{authorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> subscribe(/*@NonNull*/ @PathVariable String userId,
            /*@NonNull*/ @PathVariable String authorId) {
        subscriptionService.subscribe(userId, authorId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{userId}/subscriptions/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(/*@NonNull*/ @PathVariable String userId,
            /*@NonNull*/ @PathVariable String authorId) {
        subscriptionService.unsubscribe(userId, authorId);
    }

    @GetMapping("/users/{userId}/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public Page<SubscriptionResponse> findSubscriptions(/*@NonNull*/ @PathVariable String userId,
                                                                     Pageable pageable) {
        return subscriptionService.findSubscriptions(userId, pageable)
                .map(this::toSubscriptionResponse);
    }

    @GetMapping("/users/{userId}/subscriptions/articles")
    @ResponseStatus(HttpStatus.OK)
    public Page<CloseArticleResponse> findArticlesBySubscriptions(/*@NonNull*/ @PathVariable String userId,
                                                                               @SortDefault(sort = "a.publicationDate") Pageable pageable) {
        return articleService.findArticlesBySubscriptions(userId, pageable)
                .map(this::toCloseArticleResponse);
    }

    @GetMapping("/users/{userId}/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public Page<SubscriberResponse> findSubscribers(/*@NonNull*/ @PathVariable String userId,
                                                                 Pageable pageable) {
        return subscriptionService.findSubscribers(userId, pageable)
                .map(this::toSubscriberResponse);
    }

    /*@NonNull*/
    private SubscriptionResponse toSubscriptionResponse(/*@NonNull*/ UserEntity user) {
        PersonalInfo personalInfo = user.getPersonalInfo();
        return new SubscriptionResponse(
                user.getId(),
                personalInfo.getFirstname(),
                personalInfo.getLastname(),
                personalInfo.getNickname());
    }

    /*@NonNull*/
    private SubscriberResponse toSubscriberResponse(/*@NonNull*/ UserEntity user) {
        PersonalInfo personalInfo = user.getPersonalInfo();
        return new SubscriberResponse(user.getId(), personalInfo.getNickname());
    }

    /*@NonNull*/
    private CloseArticleResponse toCloseArticleResponse(/*@NonNull*/ ArticleEntity article) {
        final PersonalInfo personalInfo = article.getAuthor().getPersonalInfo();
        return new CloseArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getStatus().name(),
                article.getDescription(),
                article.getRating(),
                article.getPublicationDate(),
                article.getModificationDate(),
                article.getAuthor().getId(),
                personalInfo.getNickname(),
                personalInfo.getPhoto(),
                tagEntityToResponse(article.getTags()));
    }

    /*@NonNull*/
    private List<TagResponse> tagEntityToResponse(List<TagEntity> tagEntities) {
        return Optional.ofNullable(tagEntities).map(tags -> tags.stream()
                .map(tagEntity -> new TagResponse(tagEntity.getId(), tagEntity.getName()))
                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

}
