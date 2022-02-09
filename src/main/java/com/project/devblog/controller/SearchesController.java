package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.response.CloseArticleResponse;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.ArticleService;
import com.project.devblog.service.TagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import static java.util.stream.Collectors.toList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Search")
@ApiV1
@RestController
@RequiredArgsConstructor
public class SearchesController {

    private final ArticleService articleService;
    private final TagService tagService;

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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/searches/tags")
    public List<TagResponse> getByName(@NonNull @RequestParam(name = "nameContains") String substring) {
        return tagService.getByNameContains(substring).stream()
                .map(this::toResponse)
                .collect(toList());
    }

    @NonNull
    public TagResponse toResponse(@NonNull TagEntity tagEntity) {
        return new TagResponse(tagEntity.getId(), tagEntity.getName());
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