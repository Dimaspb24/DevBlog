package com.project.devblog.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class TagResponse {
    @NonNull
    private final Integer id;
    @NonNull
    private final String name;
}
