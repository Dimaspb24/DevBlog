package com.project.devblog.controller.authcontroller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.dto.request.AuthenticationRequest;
import com.project.devblog.dto.request.RegistrationRequest;
import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "Authentication")
@ApiV1
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationResponse> login(@NonNull @Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.login(request.getLogin(), request.getPassword());
    }

    @PostMapping("/auth/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
    }

    @PostMapping("/auth/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthenticationResponse> registration(@NonNull @Valid @RequestBody RegistrationRequest request) {
        return authenticationService.register(request.getLogin(), request.getPassword());
    }

    @GetMapping("/auth/checkToken")
    public ResponseEntity<String> checkValidateToken(@NonNull @RequestParam String token) {
        jwtTokenProvider.validateToken(token);
        return ResponseEntity.ok("Token is valid");
    }
}
