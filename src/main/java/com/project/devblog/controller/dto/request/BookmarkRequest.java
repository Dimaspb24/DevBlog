package com.project.devblog.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkRequest {
    @NotNull(message = "BookmarkType is required")
    private String bookmarkType;
}
