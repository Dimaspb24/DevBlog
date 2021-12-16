package com.project.devblog.controller.exceptionhandler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ErrorBody {

    private final int status;
    @NonNull
    private final String code;
    @NonNull
    private final String message;
}
