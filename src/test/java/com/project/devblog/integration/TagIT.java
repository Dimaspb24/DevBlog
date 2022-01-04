package com.project.devblog.integration;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.TagEntity;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.TagRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@FieldDefaults(level = AccessLevel.PRIVATE)
class TagIT extends AbstractIT {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    ArticleRepository articleRepository;

    @Test
    @Transactional
    void setTagToArticle() {
        ArticleEntity article1 = articleRepository.findByTitleContains("title 1").stream().findFirst().orElseThrow();
        TagEntity maven = tagRepository.findByName("maven").orElseThrow();

        article1.addTag(maven);
        articleRepository.saveAndFlush(article1);

        ArticleEntity actualArticle = articleRepository.findByTitleContains("title 1").stream().findFirst().orElseThrow();
        assertEquals(1, actualArticle.getTags().size());
    }
}
