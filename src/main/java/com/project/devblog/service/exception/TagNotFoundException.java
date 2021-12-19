package com.project.devblog.service.exception;

public class TagNotFoundException extends RuntimeException{

    public TagNotFoundException() {
        super("Tag not found");
    }
}
