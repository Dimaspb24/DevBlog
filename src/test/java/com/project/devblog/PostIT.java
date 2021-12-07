package com.project.devblog;

import com.project.devblog.model.PostEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.StatusPost;
import com.project.devblog.repository.PostRepository;
import com.project.devblog.repository.TagRepository;
import com.project.devblog.repository.UserPostRepository;
import com.project.devblog.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;

public class PostIT extends AbstractIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserPostRepository userPostRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private EntityManager entityManager;
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    public void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Test
    void check_createPostWithAuthorAndTags() {
//        UserEntity testUser1 = userRepository.findById(1).orElseThrow();
//        TagEntity tag1 = tagRepository.findById(1).orElseThrow();
//        TagEntity tag2 = tagRepository.findById(2).orElseThrow();
//        PostEntity testPost = PostEntity.builder()
//                .author(testUser1)
//                .title("Test")
//                .body("test")
//                .status(StatusPost.CREATED)
//                .description("test")
//                .tags(List.of(tag1, tag2))
//                .build();
//        postRepository.save(testPost);
//
//        transactionTemplate.executeWithoutResult(transactionStatus -> {
//            PostEntity savedPost = postRepository.findById(1).orElseThrow();
//            assertEquals(testUser1, savedPost.getAuthor());
//            assertEquals(2, savedPost.getTags().size());
//        });
    }
}
