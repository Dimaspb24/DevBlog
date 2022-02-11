package com.project.devblog.exception;

public class NonUniqueValueException extends RuntimeException {
    public NonUniqueValueException(String message) {
        super(message);
    }

    public NonUniqueValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
