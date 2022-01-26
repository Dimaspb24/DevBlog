package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.BookmarkRequest;
import com.project.devblog.controller.dto.response.BookmarkArticleResponse;
import com.project.devblog.controller.dto.response.BookmarkResponse;
import com.project.devblog.service.BookmarkService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@ApiV1
@RestController
@AllArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/users/{userId}/articles/{articleId}/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    public BookmarkResponse create(@NonNull @PathVariable String userId,
                                   @NonNull @PathVariable Integer articleId,
                                   @NonNull @Valid BookmarkRequest request) {
        return bookmarkService.create(userId, articleId, request);
    }

    @GetMapping("/users/{userId}/bookmarks/{bookmarkType}")
    @ResponseStatus(HttpStatus.OK)
    public Page<BookmarkArticleResponse> getAll(@NonNull @PathVariable String userId,
                                                @NonNull @PathVariable String bookmarkType,
                                                @NonNull Pageable pageable) {
        return bookmarkService.findAll(userId, bookmarkType, pageable);
    }

    @DeleteMapping("/users/{userId}/bookmarks/{bookmarkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@NonNull @PathVariable String userId,
                                         @NonNull @PathVariable Long bookmarkId) {
        bookmarkService.delete(bookmarkId);
        return ResponseEntity.noContent().build();
    }
}
