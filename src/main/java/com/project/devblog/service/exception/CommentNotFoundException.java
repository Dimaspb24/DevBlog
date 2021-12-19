package com.project.devblog.service.exception;

public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException() {
        super("Comment not found");
    }
}
