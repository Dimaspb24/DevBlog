package com.project.devblog.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortingParam {
    PUBLICATION_DATE("publicationDate"),
    RATING("rating");

    private final String name;
}
