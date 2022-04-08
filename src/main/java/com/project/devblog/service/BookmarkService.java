package com.project.devblog.service;

import com.project.devblog.dto.request.BookmarkRequest;
import com.project.devblog.dto.response.BookmarkArticleResponse;
import com.project.devblog.dto.response.BookmarkResponse;
import com.project.devblog.dto.response.CloseArticleResponse;
import com.project.devblog.dto.response.TagResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.BookmarkType;
import com.project.devblog.repository.UserArticleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final UserArticleRepository userArticleRepository;
    private final ArticleService articleService;
    private final UserService userService;

    @NonNull
    @Transactional
    public BookmarkResponse createOrUpdate(@NonNull String userId, @NonNull Integer articleId, @NonNull BookmarkRequest request) {
        BookmarkType bookmarkType = BookmarkType.valueOf(request.getBookmarkType());

        UserArticleEntity userArticleEntity = userArticleRepository
                .findByUserIdAndArticleId(userId, articleId)
                .map(userArt -> {
                    userArt.setBookmarkType(bookmarkType);
                    return userArt;
                }).orElseGet(() -> {
                    final UserEntity userEntity = userService.findById(userId);
                    final ArticleEntity articleEntity = articleService.findById(articleId);
                    return new UserArticleEntity(bookmarkType, userEntity, articleEntity);
                });

        UserArticleEntity savedBookmark = userArticleRepository.save(userArticleEntity);
        return new BookmarkResponse(userId, articleId, savedBookmark.getBookmarkType().name());
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<BookmarkArticleResponse> findAll(@NonNull String userId, String bookmarkType, Pageable pageable) {
        if (Objects.isNull(bookmarkType)) {
            return userArticleRepository.findByUserIdAndBookmarkTypeNotNull(userId, pageable).
                    map(this::getBookmarkArticleResponses);
        } else {
            BookmarkType type = BookmarkType.valueOf(bookmarkType);
            return userArticleRepository.findByUserIdAndBookmarkType(userId, type, pageable)
                    .map(this::getBookmarkArticleResponses);
        }
    }

    @Transactional
    public void delete(@NonNull Long bookmarkId) {
        UserArticleEntity userArticleEntity = userArticleRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException(UserArticleEntity.class, bookmarkId.toString()));

        userArticleEntity.setBookmarkType(null);
        userArticleRepository.save(userArticleEntity);
    }

    private BookmarkArticleResponse getBookmarkArticleResponses(UserArticleEntity userArticleEntity) {
        ArticleEntity article = userArticleEntity.getArticle();
        List<TagResponse> listTags = article.getTags().stream()
                .map(tagEntity -> new TagResponse(tagEntity.getId(), tagEntity.getName()))
                .collect(Collectors.toList());
        UserEntity author = article.getAuthor();
        PersonalInfo personalInfo = author.getPersonalInfo();
        CloseArticleResponse closeArticleResponse = new CloseArticleResponse(article.getId(), article.getTitle(), article.getStatus().name(),
                article.getDescription(), article.getRating(), article.getPublicationDate(), article.getModificationDate(),
                author.getId(), personalInfo.getNickname(), personalInfo.getPhoto(), listTags);

        return new BookmarkArticleResponse(
                userArticleEntity.getId(),
                userArticleEntity.getRating(),
                userArticleEntity.getBookmarkType().toString(),
                closeArticleResponse);
    }
}
