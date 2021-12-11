package com.project.devblog;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.TagRepository;
import com.project.devblog.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagIT extends AbstractIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ArticleRepository articleRepository;

    TransactionTemplate transactionTemplate;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    void setTagToArticle() {
        ArticleEntity article1 = articleRepository.findByTitleContains("title 1").stream().findFirst().orElseThrow();
        TagEntity maven = tagRepository.findByName("maven").orElseThrow();
        article1.addTag(maven);
        articleRepository.save(article1);

        entityManager.flush();

        ArticleEntity actualArticle = articleRepository.findByTitleContains("title 1").stream().findFirst().orElseThrow();
        assertEquals(1, actualArticle.getTags().size());
    }
}
