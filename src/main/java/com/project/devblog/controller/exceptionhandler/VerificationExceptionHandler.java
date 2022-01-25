package com.project.devblog.controller.exceptionhandler;

import com.project.devblog.controller.exceptionhandler.dto.ErrorBody;
import com.project.devblog.controller.exceptionhandler.dto.ErrorsEnum;
import com.project.devblog.service.exception.VerificationException;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VerificationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull VerificationException exception) {
        return new ResponseEntity<>(new ErrorBody(ErrorsEnum.BAD_REQUEST, exception.getMessage()),
                ErrorsEnum.BAD_REQUEST.getStatus());
    }
}
