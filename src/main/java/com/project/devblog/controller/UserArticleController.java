package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.dto.request.ArticleRequest;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.OpenArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.TagEntity;
import com.project.devblog.model.enums.StatusArticle;
import com.project.devblog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = UserArticleController.TAG_NAME)
@ApiV1
@RestController
@RequiredArgsConstructor
public class UserArticleController {

    public static final String TAG_NAME = "User articles";
    public static final String OPERATION_SUMMARY = "Block or unblock the article";
    private final ArticleService articleService;
    private static final Logger log = Logger.getLogger(UserArticleController.class);

    @PostMapping("/users/{userId}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public OpenArticleResponse create(@NonNull @PathVariable final String userId,
                                      @NonNull @Valid @RequestBody final ArticleRequest request) {
        log.info("The method of creating the article was called by the user={" + userId + "}");

        return toOpenArticleResponse(articleService.create(
                userId,
                request.getTitle(),
                request.getTags(),
                request.getDescription(),
                request.getBody(),
                StatusArticle.valueOf(request.getStatus())));
    }

    @GetMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public OpenArticleResponse find(@NonNull @PathVariable final String userId,
                                    @NonNull @PathVariable final Integer articleId) {
        log.info("The search method was called by the user={" + userId + "}" + " for the article={" + articleId + "}");

        return toOpenArticleResponse(articleService.findByAuthorIdAndArticleId(userId, articleId));
    }

    @GetMapping("/users/{userId}/articles")
    @ResponseStatus(HttpStatus.OK)
    public Page<CloseArticleResponse> findAll(@NonNull @PathVariable final String userId,
                                              @SortDefault(sort = "publicationDate") @ParameterObject final Pageable pageable) {
        log.info("The search method for all articles was called by the user={" + userId + "}");

        return articleService.findAllEnabled(userId, pageable)
                .map(this::toCloseArticleResponse);
    }

    @DeleteMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable final String userId,
                       @NonNull @PathVariable final Integer articleId) {
        log.info("The deletion method was called by the user={" + userId + "}" + " for the article={" + articleId + "}");

        articleService.delete(userId, articleId);
    }

    @Operation(summary = OPERATION_SUMMARY)
    @PatchMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public void enable(@NonNull @PathVariable final String userId,
                       @NonNull @PathVariable final Integer articleId,
                       @NonNull @Valid @RequestParam final Boolean enabled) {
        log.info("The publishing method was called by the user={" + userId + "}" + " for the article={" + articleId + "}");

        articleService.enable(userId, articleId, enabled);
    }

    @PutMapping("/users/{userId}/articles/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public OpenArticleResponse update(@NonNull @PathVariable final String userId,
                                      @NonNull @PathVariable final Integer articleId,
                                      @NonNull @Valid @RequestBody final ArticleRequest request) {
        log.info("The update method was called by the user={" + userId + "}" + " for the article={" + articleId + "}");

        return toOpenArticleResponse(articleService.update(
                userId,
                articleId,
                request.getTitle(),
                request.getTags(),
                request.getDescription(),
                request.getBody(),
                StatusArticle.valueOf(request.getStatus())));
    }

    @NonNull
    private OpenArticleResponse toOpenArticleResponse(@NonNull final ArticleEntity article) {
        final PersonalInfo personalInfo = article.getAuthor().getPersonalInfo();
        return new OpenArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getBody(),
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
    private CloseArticleResponse toCloseArticleResponse(@NonNull final ArticleEntity article) {
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
    private List<TagResponse> tagEntityToResponse(@NonNull final List<TagEntity> tagEntities) {
        return tagEntities.stream()
                .map(tagEntity -> new TagResponse(tagEntity.getId(), tagEntity.getName()))
                .collect(Collectors.toList());
    }
}
