package com.project.devblog.service.exception;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException() {
        super("Article not found");
    }
}
