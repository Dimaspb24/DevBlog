package com.project.devblog.service;

import com.project.devblog.controller.dto.request.BookmarkRequest;
import com.project.devblog.controller.dto.response.BookmarkArticleResponse;
import com.project.devblog.controller.dto.response.BookmarkResponse;
import com.project.devblog.controller.dto.response.CloseArticleResponse;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.ArticleEntity;
import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserArticleEntity;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.BookmarkType;
import com.project.devblog.repository.UserArticleRepository;
import com.project.devblog.service.exception.BookmarkNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BookmarkService {

    @NonNull
    private final UserArticleRepository userArticleRepository;
    @NonNull
    private final ArticleService articleService;
    @NonNull
    private final AuthUserService userService;

    @NonNull
    public BookmarkResponse create(@NonNull Integer userId, @NonNull Integer articleId, @NonNull BookmarkRequest request) {
        BookmarkType bookmarkType = BookmarkType.fromName(request.getBookmarkType());

        UserArticleEntity userArticleEntity = userArticleRepository.findByUserIdAndArticleId(userId, articleId).map(userArt -> {
            userArt.setBookmarkType(bookmarkType);
            return userArt;
        }).orElseGet(() -> {
            final UserEntity userEntity = userService.get(userId);
            final ArticleEntity articleEntity = articleService.findById(articleId);
            return new UserArticleEntity(bookmarkType, userEntity, articleEntity);
        });

        return new BookmarkResponse(userId, articleId, userArticleEntity.getBookmarkType().getName());
    }

    @NonNull
    public Page<BookmarkArticleResponse> findAll(Integer userId, String bookmarkType, Pageable pageable) {

        BookmarkType type = BookmarkType.fromName(bookmarkType);
        Page<UserArticleEntity> userBookmarks = userArticleRepository.findByUserIdAndBookmarkType(userId, type, pageable);

        List<BookmarkArticleResponse> listCloseArticleResponse = userBookmarks.stream().map(userArticleEntity -> {

            ArticleEntity article = userArticleEntity.getArticle();
            List<TagResponse> listTags = article.getTags().stream()
                    .map(tagEntity -> new TagResponse(tagEntity.getId(), tagEntity.getName()))
                    .collect(Collectors.toList());
            UserEntity author = article.getAuthor();
            PersonalInfo personalInfo = author.getPersonalInfo();
            CloseArticleResponse closeArticleResponse = new CloseArticleResponse(article.getId(), article.getTitle(), article.getStatus().name(),
                    article.getDescription(), article.getPublicationDate(), article.getModificationDate(),
                    author.getId(), personalInfo.getNickname(), personalInfo.getPhoto(), listTags);

            return new BookmarkArticleResponse(userArticleEntity.getId(), closeArticleResponse);
        }).collect(Collectors.toList());

        return new PageImpl<>(listCloseArticleResponse, pageable, listCloseArticleResponse.size());
    }

    public void delete(@NonNull Long bookmarkId) {
        UserArticleEntity userArticleEntity = userArticleRepository.findById(bookmarkId)
                .orElseThrow(BookmarkNotFoundException::new);

        userArticleEntity.setBookmarkType(null);
        userArticleRepository.save(userArticleEntity);
    }

}
