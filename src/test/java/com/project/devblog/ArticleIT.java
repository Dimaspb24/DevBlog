package com.project.devblog;

import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.TagRepository;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;


@Transactional(propagation = Propagation.NOT_SUPPORTED) // для самостоятельного управления транзакциями
public class ArticleIT extends AbstractIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserArticleRepository userArticleRepository;

    private TransactionTemplate transactionTemplate;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Test
    void check_createArticleWithAuthorAndTags() {
//        UserEntity testUser1 = userRepository.findById(1).orElseThrow();
//        TagEntity tag1 = tagRepository.findById(1).orElseThrow();
//        TagEntity tag2 = tagRepository.findById(2).orElseThrow();
//        ArticleEntity testArticle = ArticleEntity.builder()
//                .author(testUser1)
//                .title("Test")
//                .body("test")
//                .status(StatusArticle.CREATED)
//                .description("test")
//                .tags(List.of(tag1, tag2))
//                .build();
//        articleRepository.save(testArticle);
//
//        transactionTemplate.executeWithoutResult(transactionStatus -> {
//            ArticleEntity savedArticle = articleRepository.findById(1).orElseThrow();
//            assertEquals(testUser1, savedArticle.getAuthor());
//            assertEquals(2, savedArticle.getTags().size());
//        });
    }
}
