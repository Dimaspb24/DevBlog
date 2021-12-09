package com.project.devblog;

import com.project.devblog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;

@SpringBootApplication
@RequiredArgsConstructor
public class DevBlogApplication implements CommandLineRunner {

    final UserRepository userRepository;
    final ArticleRepository articleRepository;
    final UserArticleRepository userArticleRepository;
    final CommentRepository commentRepository;
    final TagRepository tagRepository;

    public static void main(String[] args) {
        SpringApplication.run(DevBlogApplication.class, args);
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
//        UserEntity user1 = UserEntity.builder()
//                .login("dima@mail.ru")
//                .password("12345678")
//                .role(Role.USER)
//                .status(StatusUser.ACTIVE)
//                .build();
//        userRepository.save(user1);
    }
}
