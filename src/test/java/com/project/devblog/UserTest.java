package com.project.devblog;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest extends AbstractIT {

    @Autowired
    UserRepository userRepository;
//    final PostRepository postRepository;
//    final UserPostRepository userPostRepository;
//    final CommentRepository commentRepository;
//    final TagRepository tagRepository;

    @Test
    public void test() {
        UserEntity user1 = UserEntity.builder()
                .login("dima@mail.ru")
                .password("12345678")
                .role(Role.USER)
                .status(StatusUser.ACTIVE)
                .build();
        userRepository.save(user1);

        userRepository.findAll().forEach(System.out::println);
    }
}
