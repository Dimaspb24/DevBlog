package com.project.devblog.integration;

import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.TagRepository;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.repository.UserRepository;
import javax.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

@FieldDefaults(level = AccessLevel.PRIVATE)
class ArticleIT extends AbstractIT {

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

//    @Test
//    @Transactional
//    void check_createArticleWithAuthorAndTags() {
//        UserEntity testUser1 = userRepository.findById(1).orElseThrow();
//        TagEntity tag1 = tagRepository.findById(1).orElseThrow();
//        TagEntity tag2 = tagRepository.findById(2).orElseThrow();
//        ArticleEntity testArticle = ArticleEntity.builder()
//                .author(testUser1)
//                .title("Number1")
//                .body("test")
//                .status(StatusArticle.CREATED)
//                .description("test")
//                .tags(List.of(tag1, tag2))
//                .build();
//
//        articleRepository.save(testArticle);
//        entityManager.flush();
//
//        ArticleEntity savedArt = articleRepository.findByTitleContains("Number1").stream().findFirst().orElseThrow();
//        assertEquals(testUser1, savedArt.getAuthor());
//        assertEquals(2, savedArt.getTags().size());
//    }
}
