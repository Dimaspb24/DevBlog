package com.project.devblog.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookmarkType {
    FAVORITE("Favorite"),
    BOOKMARK("Bookmark");

    private final String name;

    public static BookmarkType fromName(String name) {
        for (BookmarkType b : BookmarkType.values()) {
            if (b.name.equalsIgnoreCase(name)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with name " + name + " found");
    }
}
