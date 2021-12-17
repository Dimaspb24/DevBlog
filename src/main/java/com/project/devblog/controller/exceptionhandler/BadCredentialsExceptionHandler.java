package com.project.devblog.controller.exceptionhandler;

import com.project.devblog.controller.ErrorsEnum;
import com.project.devblog.controller.exceptionhandler.dto.ErrorBody;
import com.project.devblog.service.exception.UserNotFoundException;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadCredentialsExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull BadCredentialsException exception) {
        return new ResponseEntity<>(new ErrorBody(ErrorsEnum.CONFLICT.getStatus().value(),
                ErrorsEnum.CONFLICT.name(), exception.getMessage()), ErrorsEnum.CONFLICT.getStatus());
    }
}
