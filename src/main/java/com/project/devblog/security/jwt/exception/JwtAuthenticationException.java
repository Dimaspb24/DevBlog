package com.project.devblog.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException() {
        super("JWT token invalid or user not found");
    }
}
