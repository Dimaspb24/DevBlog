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
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserService userService;

    @Transactional
    public void subscribe(String userId, String authorId) {
        UserEntity user = userService.findById(userId);
        UserEntity author = userService.findById(authorId);
        user.addSubscription(author);
        userService.save(user);
    }

    @Transactional
    public void unsubscribe(String userId, String authorId) {
        UserEntity user = userService.findById(userId);
        UserEntity author = userService.findById(authorId);
        user.removeSubscription(author);
        userService.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> findSubscriptions(String userId, Pageable pageable) {
        UserEntity user = userService.findById(userId);
        Set<UserEntity> subscriptions = user.getSubscriptions();
        return new PageImpl<>(new ArrayList<>(subscriptions), pageable, subscriptions.size());
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> findSubscribers(String userId, Pageable pageable) {
        UserEntity user = userService.findById(userId);
        Set<UserEntity> subscribers = user.getSubscribers();
        return new PageImpl<>(new ArrayList<>(subscribers), pageable, subscribers.size());
    }
}
