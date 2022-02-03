package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.response.CloseArticleResponse;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Tag(name = "Articles")
@ApiV1
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/articles")
    public Page<CloseArticleResponse> findAll(@RequestParam(name = "titleContains", required = false) String name,
                                              @SortDefault(sort = "publicationDate") Pageable pageable) {
        if (Objects.isNull(name)) {
            return articleService.find(pageable).map(this::toResponse);
        }
        return articleService.getByTitleName(name, pageable).map(this::toResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/articlesByTagName")
    public Page<CloseArticleResponse> findByTagName(@NonNull @RequestParam(name = "tagName") String tagName, Pageable pageable) {
        return articleService.findArticlesByTagName(tagName, pageable)
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
