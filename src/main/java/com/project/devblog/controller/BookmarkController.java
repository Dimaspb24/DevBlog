package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.BookmarkRequest;
import com.project.devblog.controller.dto.response.BookmarkResponse;
import javax.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ApiV1
@RestController
public class BookmarkController {

    @PostMapping("/user/{userId}/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    public BookmarkResponse create(@NonNull @PathVariable String userId, @NonNull @Valid BookmarkRequest request) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/bookmarks/{bookmarkId}")
    @ResponseStatus(HttpStatus.OK)
    public BookmarkResponse get(@NonNull @PathVariable String userId, @NonNull @PathVariable String bookmarkId) {
        return toResponse();
    }

    @GetMapping("/users/{userId}/bookmarks")
    @ResponseStatus(HttpStatus.OK)
    public Page<BookmarkResponse> getAll(@NonNull @PathVariable String userId,
                                         @NonNull Pageable pageable) {
        return toResponse();
    }

    @DeleteMapping("/users/{userId}/bookmarks/{bookmarkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BookmarkResponse delete(@NonNull @PathVariable String userId, @NonNull @PathVariable String bookmarkId) {
        return toResponse();
    }

    @NonNull
    private BookmarkResponse toResponse(@NonNull Bookmark bookmark) {
        return new BookmarkResponse();
    }
}
