package com.project.devblog;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.UserPostRepository;
import com.project.devblog.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = { PersistenceConfig.class }, loader = AnnotationConfigContextLoader.class)
//@TransactionConfiguration(transactionManager = "txMgr" , defaultRollback = false)
class UserTest extends AbstractIT {

    // todo Нужно сделать так, чтобы здесь была возможность управялть транзакциями в тестах!!!

//    @Autowired
//    private SessionFactory sessionFactory;
//
//    private Session session;
//
//    @BeforeEach
//    public final void before() {
//        session = sessionFactory.openSession();
//        session.beginTransaction();
//    }
//
//    @AfterEach
//    public final void after() {
//        session.getTransaction().commit();
//        session.close();
//    }


    @Autowired
    UserRepository userRepository;
//    final PostRepository postRepository;
    @Autowired
    UserPostRepository userPostRepository;
//    final CommentRepository commentRepository;
//    final TagRepository tagRepository;

    @Test
    void printUsers() {
        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    void saveUser() {
        UserEntity user1 = UserEntity.builder()
                .login("dima@mail.ru")
                .password("12345678")
                .role(Role.USER)
                .status(StatusUser.ACTIVE)
                .build();
        userRepository.save(user1);
    }

    @Rollback(value = false)
    @Test
    @Transactional
    void addSubscriber() {
        UserEntity user1 = userRepository.findById(1).orElseThrow();
        UserEntity user2 = userRepository.findById(2).orElseThrow();
        user1.addSubscription(user2);

        System.out.println("OK");
    }

    @Test
    void checkSubscribers() {
        userPostRepository.findAll().forEach(System.out::println);
    }
}
