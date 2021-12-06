package com.project.devblog;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.UserPostRepository;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = { PersistenceConfig.class }, loader = AnnotationConfigContextLoader.class)
//@TransactionConfiguration(transactionManager = "txMgr" , defaultRollback = false)
public class UserTest extends AbstractIT {

//    @Autowired
//    EntityManager entityManager;

//    TransactionManager transactionManager = new ;

    @Autowired
    TransactionTemplate transactionTemplate;

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
    UserService userService;
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

    @Test
    void addSubscription() {
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

    @Test
    @Transactional
    void checkSubscribers() {
        UserEntity user1 = userRepository.findById(1).orElseThrow();
        user1.getSubscriptions().forEach(System.out::println);
        UserEntity user2 = userRepository.findById(2).orElseThrow();
        user2.getSubscribers().forEach(System.out::println);
    }
}
