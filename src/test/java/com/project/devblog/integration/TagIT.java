package com.project.devblog.integration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
class TagIT extends AbstractIT {

//    @Autowired
//    TagRepository tagRepository;
//    @Autowired
//    ArticleRepository articleRepository;
//
//    @Test
//    @Transactional
//    void setTagToArticle() {
//        ArticleEntity article1 = articleRepository.findByTitleContains("title 1").stream().findFirst().orElseThrow();
//        TagEntity maven = tagRepository.findByName("maven").orElseThrow();
//
//        article1.addTag(maven);
//        articleRepository.saveAndFlush(article1);
//
//        ArticleEntity actualArticle = articleRepository.findByTitleContains("title 1").stream().findFirst().orElseThrow();
//        assertEquals(1, actualArticle.getTags().size());
//    }
}
