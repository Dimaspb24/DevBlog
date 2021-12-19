package com.project.devblog.controller.exceptionhandler.dto;

import com.project.devblog.controller.ErrorsEnum;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ErrorBody {

    private final int status;
    @NonNull
    private final String code;
    @NonNull
    private final String message;

    public ErrorBody(@NonNull ErrorsEnum status, @NonNull String message) {
        this.status = status.getStatus().value();
        this.code = status.name();
        this.message = message;
    }
}
