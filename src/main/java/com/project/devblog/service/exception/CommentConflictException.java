package com.project.devblog.service.exception;

public class CommentConflictException extends RuntimeException {

    public CommentConflictException(String msg) {
        super(msg);
    }
}
