package com.project.devblog.controller.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookmarkRequest {
    @NotBlank(message = "BookmarkType is required")
    @Pattern(regexp = "(bookmark)|(favorite)")
    String bookmarkType;
}
