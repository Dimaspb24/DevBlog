package com.project.devblog.controller.exceptionhandler;

import com.project.devblog.controller.exceptionhandler.dto.ErrorBody;
import com.project.devblog.controller.exceptionhandler.dto.ErrorsEnum;
import com.project.devblog.service.exception.CommentConflictException;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentConflictExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull CommentConflictException exception) {
        return new ResponseEntity<>(new ErrorBody(ErrorsEnum.CONFLICT, exception.getMessage()),
                ErrorsEnum.CONFLICT.getStatus());
    }
}
