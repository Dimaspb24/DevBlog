package com.project.devblog.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkRequest {

    @Pattern(regexp = "(BOOKMARK)|(FAVORITE)", message = "Please provide a valid bookmark type: BOOKMARK or FAVORITE")
    String bookmarkType;
}
