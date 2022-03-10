package com.project.devblog.service;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    UserService userService;
    @InjectMocks
    SubscriptionService subscriptionService;
    @Captor
    ArgumentCaptor<UserEntity> argumentCaptorUser;

    UserEntity subscriber;
    UserEntity author;

    @BeforeEach
    void init() {
        author = UserEntity.builder()
                .id("generatedId")
                .role(Role.USER)
                .login("login@mail.ru")
                .password("encodedPassword")
                .build();
        subscriber = UserEntity.builder()
                .id("generatedId2")
                .role(Role.USER)
                .login("login2@mail.ru")
                .password("encodedPassword")
                .build();
    }


    @Test
    void subscribe() {
        doReturn(subscriber).when(userService).find(subscriber.getId());
        doReturn(author).when(userService).find(author.getId());

        subscriptionService.subscribe(subscriber.getId(), author.getId());

        verify(userService, times(2)).find(anyString());
        verify(userService).save(argumentCaptorUser.capture());
        assertThat(argumentCaptorUser.getValue().getSubscriptions()).hasSize(1);
    }

    @Test
    void unsubscribe() {
        subscriber.addSubscription(author);
        doReturn(subscriber).when(userService).find(subscriber.getId());
        doReturn(author).when(userService).find(author.getId());

        subscriptionService.unsubscribe(subscriber.getId(), author.getId());

        verify(userService, times(2)).find(anyString());
        verify(userService).save(argumentCaptorUser.capture());
        assertThat(argumentCaptorUser.getValue().getSubscriptions()).isEmpty();
    }

    @Test
    void findSubscriptions() {
        subscriber.addSubscription(author);
        doReturn(subscriber).when(userService).find(subscriber.getId());

        Page<UserEntity> subscriptions = subscriptionService.findSubscriptions(subscriber.getId(), Pageable.unpaged());

        verify(userService, times(1)).find(subscriber.getId());
        assertThat(subscriptions.getContent()).hasSize(1);
    }

    @Test
    void findSubscribers() {
        subscriber.addSubscription(author);
        author.getSubscribers().add(subscriber);
        doReturn(author).when(userService).find(author.getId());

        Page<UserEntity> subscribers = subscriptionService.findSubscribers(author.getId(), Pageable.unpaged());

        verify(userService, times(1)).find(author.getId());
        assertThat(subscribers.getContent()).hasSize(1);
    }
}