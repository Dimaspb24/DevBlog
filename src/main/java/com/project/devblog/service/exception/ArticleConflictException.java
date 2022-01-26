package com.project.devblog.service.exception;

public class ArticleConflictException extends RuntimeException {

    public ArticleConflictException(String msg) {
        super(msg);
    }
}
