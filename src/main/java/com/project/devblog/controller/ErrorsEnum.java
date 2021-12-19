package com.project.devblog.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorsEnum {
    NOT_FOUND(HttpStatus.NOT_FOUND),
    CONFLICT(HttpStatus.CONFLICT),
    BAD_REQUEST(HttpStatus.BAD_REQUEST);

    @NonNull
    private final HttpStatus status;
}
