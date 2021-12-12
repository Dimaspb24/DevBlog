package com.project.devblog.service;

import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void addSubscription(Integer userId, Integer userForSubscriptionId) {
        UserEntity user1 = userRepository.getById(userId);
        UserEntity user2 = userRepository.getById(userForSubscriptionId);
        user1.addSubscription(user2);
    }
}
