package com.project.devblog.integration;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.BookmarkType;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
class UserIT extends AbstractIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserArticleRepository userArticleRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    void checkSaveUserWithDefaultFieldsInDB() {
        String login = "test@mail.ru";
        UserEntity testUser = UserEntity.builder()
                .login(login)
                .password("123456789")
                .role(Role.USER)
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
        UserEntity user1 = userRepository.findById(1).orElseThrow();
        UserEntity user2 = userRepository.findById(2).orElseThrow();

        user1.addSubscription(user2);
        entityManager.flush();

        List<UserEntity> subscriptionsUser1 = user1.getSubscriptions();
        List<UserEntity> subscribersUser2 = user2.getSubscribers();

        assertEquals(subscriptionsUser1.size(), subscribersUser2.size());
        assertTrue(subscriptionsUser1.contains(user2));
        assertTrue(subscribersUser2.contains(user1));
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
