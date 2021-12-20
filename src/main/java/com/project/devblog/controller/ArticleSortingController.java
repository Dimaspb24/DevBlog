package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.response.CloseArticleResponse;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.enums.SortOrder;
import com.project.devblog.model.enums.SortingParam;
import com.project.devblog.service.ArticleService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@ApiV1
@RestController
@AllArgsConstructor
public class ArticleSortingController {

    @NonNull
    private final ArticleService articleService;

    @GetMapping("/topic/articles")
    @ResponseStatus(HttpStatus.OK)
    public Page<CloseArticleResponse> sorting(@NonNull @Valid @RequestParam SortingParam sortingParam,
                                              @NonNull @Valid @RequestParam SortOrder sortOrder,
                                              @PageableDefault Pageable pageable) {
        if (sortingParam == SortingParam.RATING) {
            return articleService.getSortedListByRating(sortOrder, pageable)
                    .map(this::toResponse);
        } else {
            return articleService.getSortedListByPublicationDate(sortOrder, pageable)
                    .map(this::toResponse);
        }
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