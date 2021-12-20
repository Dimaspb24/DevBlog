package com.project.devblog.controller.exceptionhandler;

import com.project.devblog.controller.exceptionhandler.dto.ErrorBody;
import com.project.devblog.controller.exceptionhandler.dto.ErrorsEnum;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadCredentialsExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull BadCredentialsException exception) {
        return new ResponseEntity<>(new ErrorBody(ErrorsEnum.CONFLICT, exception.getMessage()),
                ErrorsEnum.CONFLICT.getStatus());
    }
}
