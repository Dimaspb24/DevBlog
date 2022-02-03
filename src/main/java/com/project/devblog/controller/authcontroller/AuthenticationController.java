package com.project.devblog.controller.authcontroller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.AuthenticationRequest;
import com.project.devblog.controller.dto.request.RegistrationRequest;
import com.project.devblog.controller.dto.response.AuthenticationResponse;
import com.project.devblog.controller.dto.response.RegistrationResponse;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationResponse> login(@NonNull @Valid @RequestBody AuthenticationRequest request) {
        final String login = request.getLogin();

        if (!userService.findByLogin(login).getEnabled()) {
            throw new VerificationException("This account is not verified");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, request.getPassword()));
        final UserEntity user = userService.findByLogin(login);
        final String token = jwtTokenProvider.createToken(login, user.getRole());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(toResponse(user.getId(), login, token));
    }

    @PostMapping("/auth/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, auth);
    }

    @PostMapping("/auth/registration")
    @ResponseStatus(HttpStatus.OK)
    public RegistrationResponse registration(@NonNull @Valid @RequestBody RegistrationRequest request) {
        if (!userService.isExists(request.getLogin())) {
            return toResponse(userService.register(
                    request.getLogin(),
                    request.getPassword()));
        } else {
            throw new BadCredentialsException("Login already exists");
        }
    }

    @NonNull
    private RegistrationResponse toResponse(@NonNull UserEntity user) {
        return new RegistrationResponse(user.getId(), user.getLogin());
    }

    @NonNull
    private AuthenticationResponse toResponse(@NonNull String id, @NonNull String login, @NonNull String token) {
        return new AuthenticationResponse(id, login, token);
    }
}
