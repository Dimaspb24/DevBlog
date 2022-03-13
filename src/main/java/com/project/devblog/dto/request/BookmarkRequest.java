package com.project.devblog.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookmarkRequest {

    @Pattern(regexp = "(BOOKMARK)|(FAVORITE)", message = "Please provide a valid bookmark type: BOOKMARK or FAVORITE")
    String bookmarkType;
}
