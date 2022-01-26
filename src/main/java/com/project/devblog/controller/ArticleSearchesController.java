package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.response.CloseArticleResponse;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.ArticleService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@ApiV1
@RestController
@AllArgsConstructor
public class ArticleSearchesController {

    @NonNull
    private final ArticleService articleService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/searches/articles")
    public Page<CloseArticleResponse> getByTitleName(@RequestParam(name = "titleContains") String name, @SortDefault(sort = "publicationDate") Pageable pageable) {
        return articleService.getByTitleName(name, pageable)
                .map(this::toResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/searches/articlesByTag")
    public Page<CloseArticleResponse> findArticlesByTagName(@NonNull @RequestParam(name = "tag") String tag, Pageable pageable) {
        return articleService.findArticlesByTagName(tag, pageable)
                .map(this::toResponse);
    }

    @NonNull
    private CloseArticleResponse toResponse(@NonNull ArticleEntity article) {
        final PersonalInfo personalInfo = article.getAuthor().getPersonalInfo();
        return new CloseArticleResponse(article.getId(), article.getTitle(), article.getStatus().name(), article.getDescription(),
                article.getPublicationDate(), article.getModificationDate(), article.getAuthor().getId(), personalInfo.getNickname(),
                personalInfo.getPhoto(), tagEntityToResponse(article.getTags()));
    }

    @NonNull
    private List<TagResponse> tagEntityToResponse(@NonNull List<TagEntity> tagEntities) {
        return tagEntities.stream()
                .map(tagEntity -> new TagResponse(tagEntity.getId(), tagEntity.getName()))
                .collect(Collectors.toList());
    }
}
