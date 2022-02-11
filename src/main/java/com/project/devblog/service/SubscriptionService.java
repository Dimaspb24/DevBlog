package com.project.devblog.service;

import com.project.devblog.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserService userService;

    public void subscribe(String userId, String authorId) {
        UserEntity user = userService.find(userId);
        UserEntity author = userService.find(authorId);
        user.addSubscription(author);
        userService.save(user);
    }

    public void unsubscribe(String userId, String authorId) {
        UserEntity user = userService.find(userId);
        UserEntity author = userService.find(authorId);
        user.removeSubscription(author);
        userService.save(user);
    }

    public Page<UserEntity> findSubscriptions(String userId, Pageable pageable) {
        UserEntity user = userService.find(userId);
        Set<UserEntity> subscriptions = user.getSubscriptions();
        return new PageImpl<>(new ArrayList<>(subscriptions), pageable, subscriptions.size());
    }

    public Page<UserEntity> findSubscribers(String userId, Pageable pageable) {
        UserEntity user = userService.find(userId);
        Set<UserEntity> subscribers = user.getSubscribers();
        return new PageImpl<>(new ArrayList<>(subscribers), pageable, subscribers.size());
    }
}
