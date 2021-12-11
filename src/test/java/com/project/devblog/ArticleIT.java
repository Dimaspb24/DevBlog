package com.project.devblog;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.TagRepository;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
// @Transactional(propagation = Propagation.NOT_SUPPORTED) // для самостоятельного управления транзакциями
public class ArticleIT extends AbstractIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    UserArticleRepository userArticleRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    void check_createArticleWithAuthorAndTags() {
        UserEntity testUser1 = userRepository.findById(1).orElseThrow();
        TagEntity tag1 = tagRepository.findById(1).orElseThrow();
        TagEntity tag2 = tagRepository.findById(2).orElseThrow();
        ArticleEntity testArticle = ArticleEntity.builder()
                .author(testUser1)
                .title("Number1")
                .body("test")
                .status(StatusArticle.CREATED)
                .description("test")
                .tags(List.of(tag1, tag2))
                .build();
        articleRepository.save(testArticle);

        entityManager.flush();

        ArticleEntity savedArt = articleRepository.findByTitleContains("Number1").stream().findFirst().orElseThrow();
        assertEquals(testUser1, savedArt.getAuthor());
        assertEquals(2, savedArt.getTags().size());
    }
}
