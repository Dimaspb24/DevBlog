package com.project.devblog.controller.authcontroller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.exception.JwtAuthenticationException;
import com.project.devblog.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Jwt token check")
@ApiV1
@RestController
@RequiredArgsConstructor
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
