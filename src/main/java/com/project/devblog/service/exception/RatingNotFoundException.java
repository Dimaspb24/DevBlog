package com.project.devblog.service.exception;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException() {
        super("Rating not found");
    }
}
