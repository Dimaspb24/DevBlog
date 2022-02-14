package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.CommentResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.CommentEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.ArticleService;
import com.project.devblog.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Articles")
@ApiV1
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/articles")
    public Page<CloseArticleResponse> findAll(@RequestParam(name = "titleContains", required = false) String titleContains,
                                              @RequestParam(name = "tagName", required = false) String tagName,
                                              @SortDefault(sort = "publicationDate") Pageable pageable) {
        return articleService.findAll(titleContains, tagName, pageable)
                .map(this::toResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/articles/{articleId}/comments")
    public Page<CommentResponse> findAll(@NonNull @PathVariable Integer articleId,
                                         @SortDefault(sort = "creationDate") Pageable pageable) {
        return commentService.findAllByArticleId(articleId, pageable)
                .map(this::toResponse);
    }

    @NonNull
    private CommentResponse toResponse(@NonNull CommentEntity comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getMessage(),
                comment.getAuthor().getPersonalInfo().getNickname(),
                comment.getReceiver().getPersonalInfo().getNickname(),
                comment.getCreationDate(),
                comment.getArticle().getId());
    }

    @NonNull
    private CloseArticleResponse toResponse(@NonNull ArticleEntity article) {
        final PersonalInfo personalInfo = article.getAuthor().getPersonalInfo();
        return new CloseArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getStatus().name(),
                article.getDescription(),
                article.getRating(),
                article.getPublicationDate(),
                article.getModificationDate(),
                article.getAuthor().getId(),
                personalInfo.getNickname(),
                personalInfo.getPhoto(),
                tagEntityToResponse(article.getTags()));
    }

    @NonNull
    private List<TagResponse> tagEntityToResponse(@NonNull List<TagEntity> tagEntities) {
        return tagEntities.stream()
                .map(tagEntity -> new TagResponse(tagEntity.getId(), tagEntity.getName()))
                .collect(Collectors.toList());
    }
}
