package com.project.devblog.controller.exceptionhandler;

import com.project.devblog.controller.ErrorsEnum;
import com.project.devblog.controller.exceptionhandler.dto.ErrorBody;
import com.project.devblog.service.exception.CommentNotFoundException;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentNotFoundExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull CommentNotFoundException exception) {
        return new ResponseEntity<>(new ErrorBody(ErrorsEnum.NOT_FOUND, exception.getMessage()),
                ErrorsEnum.NOT_FOUND.getStatus());
    }
}
