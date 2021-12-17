package com.project.devblog.controller.exceptionhandler;

import com.project.devblog.controller.ErrorsEnum;
import com.project.devblog.controller.exceptionhandler.dto.ErrorBody;
import com.project.devblog.security.jwt.exception.JwtAuthenticationException;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JwtAuthenticationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull JwtAuthenticationException exception) {
        return new ResponseEntity<>(new ErrorBody(ErrorsEnum.NOT_FOUND.getStatus().value(),
                ErrorsEnum.NOT_FOUND.name(), exception.getMessage()), ErrorsEnum.NOT_FOUND.getStatus());
    }
}
