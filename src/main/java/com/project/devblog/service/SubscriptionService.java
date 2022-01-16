package com.project.devblog.service;

import com.project.devblog.model.UserEntity;
import com.project.devblog.service.idgenerator.Generator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SubscriptionService {

    @NonNull
    private final UserService userService;

    public void subscribe(String userId, String authorId) {
        UserEntity user = userService.get(userId);
        UserEntity author = userService.get(authorId);
        user.addSubscription(author);
        userService.save(user);
    }

    public void unsubscribe(String userId, String authorId) {
        UserEntity user = userService.get(userId);
        UserEntity author = userService.get(authorId);
        user.getSubscriptions().remove(author);
        userService.save(user);
    }

    public Page<UserEntity> findSubscriptions(String userId, Pageable pageable) {
        UserEntity user = userService.get(userId);
        List<UserEntity> subscriptions = user.getSubscriptions();
        return new PageImpl<>(subscriptions, pageable, subscriptions.size());
    }

    public Page<UserEntity> findSubscribers(String userId, Pageable pageable) {
        UserEntity user = userService.get(userId);
        List<UserEntity> subscribers = user.getSubscribers();
        return new PageImpl<>(subscribers, pageable, subscribers.size());
    }
}
