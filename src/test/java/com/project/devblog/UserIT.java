package com.project.devblog;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;

public class UserIT extends AbstractIT {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserArticleRepository userArticleRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private EntityManager entityManager;
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    public void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }
//    final PostRepository postRepository;
//    final CommentRepository commentRepository;
//    final TagRepository tagRepository;

    @Rollback
    @Test
    void testA_checkSaveUserWithDefaultFieldsInDB() {
        UserEntity user1 = UserEntity.builder()
                .login("dima@mail.ru")
                .password("12345678")
                .role(Role.USER)
                .status(StatusUser.ACTIVE)
                .build();
        userRepository.save(user1);
    }

    @Test
    void testB_addSubscription() {
        userService.addSubscription(1, 2);

        transactionTemplate.executeWithoutResult(transactionStatus -> {
            UserEntity user1 = userRepository.findById(1).orElseThrow();
            UserEntity user2 = userRepository.findById(2).orElseThrow();

            List<UserEntity> subscriptionsUser1 = user1.getSubscriptions();
            List<UserEntity> subscribersUser2 = user2.getSubscribers();

            System.out.println("subscriptionsUser1 = " + subscriptionsUser1);
            System.out.println("subscribersUser2 = " + subscribersUser2);

            assertEquals(subscriptionsUser1.size(), subscribersUser2.size());
            assertTrue(subscriptionsUser1.contains(user2));
            assertTrue(subscribersUser2.contains(user1));
        });
    }

}
