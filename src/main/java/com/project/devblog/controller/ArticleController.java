package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.ArticleRequest;
import com.project.devblog.controller.dto.response.CloseArticleResponse;
import com.project.devblog.controller.dto.response.OpenArticleResponse;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.service.ArticleService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApiV1
@RestController
@AllArgsConstructor
public class ArticleController {

    @NonNull
    private final ArticleService articleService;

    @PostMapping("/users/{userId}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public OpenArticleResponse create(@NonNull @PathVariable String userId, @NonNull @Valid @RequestBody ArticleRequest request) {
        return toOpenArticleResponse(articleService.create(userId, request.getTitle(), request.getTags(), request.getDescription(),
                request.getBody(), StatusArticle.valueOf(request.getStatus())));
    }

    @GetMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public OpenArticleResponse get(@NonNull @PathVariable String userId, @NonNull @PathVariable Integer articleId) {
        return toOpenArticleResponse(articleService.get(userId, articleId));
    }

    @GetMapping("/users/{userId}/articles")
    @ResponseStatus(HttpStatus.OK)
    public Page<CloseArticleResponse> getAll(@NonNull @PathVariable String userId, @PageableDefault Pageable pageable) {
        return articleService.getAll(userId, pageable)
                .map(this::toCloseArticleResponse);
    }

    @GetMapping("/users/{userId}/articlesBySubscriptions")
    @ResponseStatus(HttpStatus.OK)
    public Page<CloseArticleResponse> findArticlesBySubscriptions(@NonNull @PathVariable String userId, Pageable pageable) {
        return articleService.findArticlesBySubscriptions(userId, pageable)
                .map(this::toCloseArticleResponse);
    }

    @DeleteMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable String userId, @NonNull @PathVariable Integer articleId) {
        articleService.delete(userId, articleId);
    }

    @PutMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public OpenArticleResponse update(@NonNull @PathVariable String userId, @NonNull @PathVariable Integer articleId,
                                      @NonNull @Valid ArticleRequest request) {
        return toOpenArticleResponse(articleService.update(userId, articleId, request.getTitle(), request.getTags(),
                request.getDescription(), request.getBody(), StatusArticle.valueOf(request.getStatus())));
    }

    @NonNull
    private OpenArticleResponse toOpenArticleResponse(@NonNull ArticleEntity article) {
        PersonalInfo personalInfo = article.getAuthor().getPersonalInfo();
        return new OpenArticleResponse(article.getId(), article.getTitle(), article.getBody(), article.getStatus().name(),
                article.getDescription(), article.getPublicationDate(), article.getModificationDate(), article.getAuthor().getId(),
                personalInfo.getNickname(), personalInfo.getPhoto(), tagEntityToResponse(article.getTags()));
    }

    @NonNull
    private CloseArticleResponse toCloseArticleResponse(@NonNull ArticleEntity article) {
        final PersonalInfo personalInfo = article.getAuthor().getPersonalInfo();
        return new CloseArticleResponse(article.getId(), article.getTitle(), article.getStatus().name(), article.getDescription(),
                article.getPublicationDate(), article.getModificationDate(), article.getAuthor().getId(), personalInfo.getNickname(),
                personalInfo.getPhoto(), tagEntityToResponse(article.getTags()));
    }

    private List<TagResponse> tagEntityToResponse(List<TagEntity> tagEntities) {
        return Optional.ofNullable(tagEntities).map(tags ->
                tags.stream()
                        .map(tagEntity -> new TagResponse(tagEntity.getId(), tagEntity.getName()))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
