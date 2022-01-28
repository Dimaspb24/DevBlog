package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.security.jwt.JwtTokenProvider;
import com.project.devblog.security.jwt.exception.JwtAuthenticationException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ApiV1
@RestController
@AllArgsConstructor
public class JwtController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkValidateToken(@NonNull @RequestParam String token) {
        try {
            jwtTokenProvider.validateToken(token);
            return ResponseEntity.ok(token);
        } catch (JwtAuthenticationException e) {
            return new ResponseEntity<>(token, HttpStatus.UNAUTHORIZED);
        }
    }
}
