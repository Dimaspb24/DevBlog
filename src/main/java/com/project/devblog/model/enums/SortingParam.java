package com.project.devblog.model.enums;

public enum SortingParam {
    PUBLICATION_DATE("publicationDate"),
    RATING("rating");

    private final String name;

    SortingParam(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
