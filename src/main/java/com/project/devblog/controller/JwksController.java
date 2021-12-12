package com.project.devblog.controller;

import com.project.devblog.security.jwt.JwtAuthenticationException;
import com.project.devblog.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/jwks")
@AllArgsConstructor
public class JwksController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/checkToken/{token}")
    public ResponseEntity checkValidateToken(@PathVariable String token) {
        Map<Object, Object> model = new HashMap<>();
        try {
            boolean check = jwtTokenProvider.validateToken(token);
            model.put("validate", check);
            return ResponseEntity.ok(model);
        } catch (JwtAuthenticationException e) {
            model.put("validate", false);
            return ResponseEntity.ok(model);
        }
    }
}
