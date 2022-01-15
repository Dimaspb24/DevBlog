package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.security.jwt.exception.JwtAuthenticationException;
import com.project.devblog.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ApiV1
@RestController
@AllArgsConstructor
public class JwksController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/checkToken")
    public ResponseEntity<String> checkValidateToken(@NonNull @RequestParam String token) {
        try {
            jwtTokenProvider.validateToken(token);
            return ResponseEntity.ok(token);
        } catch (JwtAuthenticationException e) {
            return new ResponseEntity<>(token, HttpStatus.UNAUTHORIZED);
        }
    }
}
