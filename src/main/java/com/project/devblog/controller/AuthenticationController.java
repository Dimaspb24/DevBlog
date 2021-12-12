package com.project.devblog.controller;

import com.project.devblog.dto.AuthenticationRequestDto;
import com.project.devblog.model.UserEntity;
import com.project.devblog.security.jwt.JwtAuthenticationException;
import com.project.devblog.security.jwt.JwtTokenProvider;
import com.project.devblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String login = requestDto.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, requestDto.getPassword()));
            UserEntity user = userService.findByLogin(login);

            if (user == null) {
                throw new UsernameNotFoundException("User with login: " + login + " NOT FOUND");
            }

            String token = jwtTokenProvider.createToken(login);

            Map<Object, Object> response = new HashMap<>();

            response.put("login", login);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid login or password");
        }
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody AuthenticationRequestDto requestDto) {
        String login = requestDto.getLogin();
        String password = requestDto.getPassword();

        if (userService.findByLogin(login) == null && !login.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setLogin(login);
            user.setPassword(password);

            userService.register(user);

            Map<Object, Object> response = new HashMap<>();
            response.put("login", login);
            response.put("password", user.getPassword());

            return ResponseEntity.ok(response);
        } else {
            throw new BadCredentialsException("Login already exists");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, auth);
        return ResponseEntity.ok(null);
    }

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
