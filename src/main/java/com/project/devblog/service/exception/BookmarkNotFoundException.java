package com.project.devblog.service.exception;

public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException() {
        super("Bookmark not found");
    }
}
