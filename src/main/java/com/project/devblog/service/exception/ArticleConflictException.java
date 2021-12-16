package com.project.devblog.service.exception;

public class ArticleConflictException extends RuntimeException {

    public ArticleConflictException() {
        super("Article is disabled");
    }
}
