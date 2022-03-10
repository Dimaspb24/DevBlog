package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.repository.CommentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ArticleService articleService;
    @MockBean
    private UserService userService;
    private static UserEntity author;
    private static UserEntity receiver;
    private static ArticleEntity article;
    private static CommentEntity comment;
    private static CommentEntity comment2;

    @BeforeAll
    static void init() {
        author = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .login("author@mail.ru")
                .password("author")
                .role(Role.USER)
                .build();
        receiver = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .login("receiver@mail.ru")
                .password("receiver")
                .role(Role.USER)
                .build();
        article = ArticleEntity.builder()
                .id(1)
                .author(new UserEntity())
                .title("title")
                .body("body")
                .status(StatusArticle.PUBLISHED)
                .description("description")
                .build();
        comment = CommentEntity.builder()
                .id(1L)
                .message("message")
                .article(article)
                .author(author)
                .receiver(receiver)
                .enabled(true)
                .build();
        comment2 = CommentEntity.builder()
                .id(2L)
                .message("message2")
                .article(article)
                .author(author)
                .receiver(receiver)
                .enabled(true)
                .build();
    }

    @Test
    void createTest() throws Exception {
        when(articleService.find(author.getId(), article.getId()))
                .thenReturn(article);
        when(userService.find(author.getId()))
                .thenReturn(author);
        when(userService.find(receiver.getId()))
                .thenReturn(receiver);
        when(commentRepository.save(any(CommentEntity.class)))
                .thenReturn(comment);

        final CommentEntity createdComment = commentService.create(author.getId(), article.getId(),
                comment.getMessage(), receiver.getId());

        assertThat(createdComment.getId()).isEqualTo(comment.getId());
        assertThat(createdComment.getAuthor()).isEqualTo(author);
        assertThat(createdComment.getArticle()).isEqualTo(article);
        assertThat(createdComment.getMessage()).isEqualTo(comment.getMessage());
        assertThat(createdComment.getReceiver()).isEqualTo(receiver);
    }

    @Test
    void findTest() throws Exception {
        when(commentRepository.findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(comment.getId(), author.getId(),
                        article.getId()))
                .thenReturn(Optional.of(comment));

        final CommentEntity foundComment = commentService.find(comment.getId(), author.getId(), article.getId());

        assertThat(foundComment.getId()).isEqualTo(comment.getId());
        assertThat(foundComment.getAuthor()).isEqualTo(author);
        assertThat(foundComment.getArticle()).isEqualTo(article);
        assertThat(foundComment.getMessage()).isEqualTo(comment.getMessage());
        assertThat(foundComment.getReceiver()).isEqualTo(receiver);
    }

    @Test
    void findTestWithNotFoundComment() throws Exception {
        final Long id = 5L;
        when(commentRepository.findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(id, author.getId(),
                        article.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.find(id, author.getId(), article.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("%s with id=%s not found", CommentEntity.class.getSimpleName(), id));
    }

    @Test
    void findAllByAuthorIdAndArticleIdTest() throws Exception {
        final Page<CommentEntity> page = new PageImpl<>(List.of(comment, comment2));
        final Pageable pageable = Pageable.ofSize(2);

        when(commentRepository.findAllByAuthorIdAndArticleIdAndEnabledIsTrue(author.getId(), article.getId(),
                        pageable))
                .thenReturn(page);

        final Page<CommentEntity> foundPage = commentService.findAllByAuthorIdAndArticleId(author.getId(), article.getId(),
                pageable);

        assertThat(foundPage.getContent()).containsExactly(comment, comment2);
    }

    @Test
    void findAllByArticleIdTest() throws Exception {
        final Page<CommentEntity> page = new PageImpl<>(List.of(comment, comment2));
        final Pageable pageable = Pageable.ofSize(2);

        when(commentRepository.findAllByArticleIdAndEnabledIsTrue(article.getId(), pageable))
                .thenReturn(page);

        final Page<CommentEntity> foundPage = commentService.findAllByArticleId(article.getId(), pageable);

        assertThat(foundPage.getContent()).containsExactly(comment, comment2);
    }

    @Test
    void enableTest() throws Exception {
        when(commentRepository.findByIdAndAuthorIdAndArticleId(comment.getId(), author.getId(), article.getId()))
                .thenReturn(Optional.of(comment));
        when(commentRepository.save(any(CommentEntity.class)))
                .thenReturn(comment);

        commentService.enable(comment.getId(), author.getId(), article.getId(), true);
        assertThat(comment.getEnabled()).isTrue();

        commentService.enable(comment.getId(), author.getId(), article.getId(), false);
        assertThat(comment.getEnabled()).isFalse();
    }

    @Test
    void enableTestWithNotFoundComment() throws Exception {
        final Long id = 5L;
        when(commentRepository.findByIdAndAuthorIdAndArticleId(id, author.getId(), article.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.enable(id, author.getId(), article.getId(), true))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("%s with id=%s not found", CommentEntity.class.getSimpleName(), id));
    }

    @Test
    void deleteTest() throws Exception {
        when(commentRepository.findByIdAndAuthorIdAndArticleId(comment.getId(), author.getId(), article.getId()))
                .thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(any());

        commentService.delete(comment.getId(), author.getId(), article.getId());

        verify(commentRepository, times(1)).delete(any());
    }

    @Test
    void deleteTestWithNotFoundComment() throws Exception {
        final Long id = 5L;
        when(commentRepository.findByIdAndAuthorIdAndArticleId(id, author.getId(), article.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.delete(id, author.getId(), article.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("%s with id=%s not found", CommentEntity.class.getSimpleName(), id));
    }

    @Test
    void updateTest() throws Exception {
        when(commentRepository.findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(comment.getId(), author.getId(),
                        article.getId()))
                .thenReturn(Optional.of(comment));
        when(commentRepository.save(any(CommentEntity.class)))
                .thenReturn(comment);

        final String updatedMessage = "updatedMessage";
        final CommentEntity updatedComment = commentService.update(comment.getId(), updatedMessage, article.getId(),
                author.getId());

        assertThat(updatedComment.getMessage()).isEqualTo(updatedMessage);
    }

    @Test
    void updateTestWithNotFoundComment() throws Exception {
        final Long id = 5L;
        when(commentRepository.findByIdAndAuthorIdAndArticleIdAndEnabledIsTrue(id, author.getId(),
                        article.getId()))
                .thenReturn(Optional.empty());

        final String updatedMessage = "updatedMessage";
        assertThatThrownBy(() -> commentService.update(id, updatedMessage, article.getId(), author.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("%s with id=%s not found", CommentEntity.class.getSimpleName(), id));
    }
}
