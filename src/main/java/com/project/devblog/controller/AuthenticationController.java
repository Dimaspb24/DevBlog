package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.AuthenticationRequest;
import com.project.devblog.controller.dto.response.AuthenticationResponse;
import com.project.devblog.security.jwt.JwtTokenProvider;
import com.project.devblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiV1
@RestController
@AllArgsConstructor
public class AuthenticationController {

    @NonNull
    private final AuthenticationManager authenticationManager;
    @NonNull
    private final JwtTokenProvider jwtTokenProvider;
    @NonNull
    private final UserService userService;

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(@NonNull @Valid AuthenticationRequest request) {
        final String login = request.getLogin();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, request.getPassword()));
        final String token = jwtTokenProvider.createToken(login);
        final Integer userId = userService.findByLogin(login).getId();

        return toResponse(userId, login, token);
    }

    @PostMapping("/auth/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, auth);
    }

    @NonNull
    private AuthenticationResponse toResponse(@NonNull Integer id, @NonNull String login, @NonNull String token) {
        return new AuthenticationResponse(id, login, token);
    }
}
