package com.project.devblog.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkRequest {
    @NotBlank(message = "BookmarkType is required")
    @Pattern(regexp = "(BOOKMARK)|(FAVORITE)")
    String bookmarkType;
}
