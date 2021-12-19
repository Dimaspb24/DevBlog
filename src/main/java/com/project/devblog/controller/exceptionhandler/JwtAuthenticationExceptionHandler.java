package com.project.devblog.controller.exceptionhandler;

import com.project.devblog.controller.exceptionhandler.dto.ErrorBody;
import com.project.devblog.model.enums.ErrorsEnum;
import com.project.devblog.security.jwt.exception.JwtAuthenticationException;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JwtAuthenticationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(@NonNull JwtAuthenticationException exception) {
        return new ResponseEntity<>(new ErrorBody(ErrorsEnum.UNAUTHORIZED.getStatus().value(),
                ErrorsEnum.UNAUTHORIZED.name(), exception.getMessage()), ErrorsEnum.UNAUTHORIZED.getStatus());
    }
}
