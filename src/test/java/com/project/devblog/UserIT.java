package com.project.devblog;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.BookmarkType;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.AuthUserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserIT extends AbstractIT {

    @Autowired
    AuthUserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserArticleRepository userArticleRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    EntityManager entityManager;
    TransactionTemplate transactionTemplate;

    @BeforeEach
    public void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Test
    @Transactional
    void checkSaveUserWithDefaultFieldsInDB() {
        String login = "test@mail.ru";
        UserEntity testUser = UserEntity.builder()
                .login(login)
                .password("123456789")
                .role(Role.USER)
                .status(StatusUser.ACTIVE)
                .build();
        userRepository.save(testUser);

        entityManager.refresh(testUser);

        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow();

        assertEquals(login, userEntity.getLogin());
        assertNotNull(userEntity.getPersonalInfo().getNickname());
    }

    @Test
    @Transactional
    void addSubscription() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            UserEntity user1 = userRepository.findById(1).orElseThrow();
            UserEntity user2 = userRepository.findById(2).orElseThrow();

            user1.addSubscription(user2);

            entityManager.flush();

            List<UserEntity> subscriptionsUser1 = user1.getSubscriptions();
            List<UserEntity> subscribersUser2 = user2.getSubscribers();

            System.out.println("subscriptionsUser1 = " + subscriptionsUser1);
            System.out.println("subscribersUser2 = " + subscribersUser2);

            assertEquals(subscriptionsUser1.size(), subscribersUser2.size());
            assertTrue(subscriptionsUser1.contains(user2));
            assertTrue(subscribersUser2.contains(user1));
        });
    }

    @Test
    @Transactional
    void addRelationArticleToUser() {
        final int rating = 5;
        UserEntity user = userRepository.findById(1).orElseThrow();
        ArticleEntity article = articleRepository.findById(1).orElseThrow();
        UserArticleEntity userArticle = UserArticleEntity.builder()
                .user(user)
                .article(article)
                .rating(rating)
                .bookmarkType(BookmarkType.FAVORITE)
                .build();
        user.addRelationArticle(userArticle);

        entityManager.flush();

        UserArticleEntity savedUserArticle = userArticleRepository.findByUserIdAndArticleIdAndArticleEnabledIsTrue(user.getId(), article.getId()).orElseThrow();
        assertEquals(rating, savedUserArticle.getRating());
    }

}
