package com.project.devblog;

import com.project.devblog.model.TagEntity;
import com.project.devblog.repository.ArticleRepository;
import com.project.devblog.repository.TagRepository;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

public class TagIT extends AbstractIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;

    private TransactionTemplate transactionTemplate;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private EntityManager entityManager;

//    @BeforeEach
//    public void setUp() {
//        transactionTemplate = new TransactionTemplate(transactionManager);
//    }

    @Test
    @Transactional
    void createSelectTag() {
        TagEntity tag1 = TagEntity.builder().name("1").build();
        tagRepository.save(tag1);
        assertNotNull(tagRepository.findByName("1"));
    }

}
