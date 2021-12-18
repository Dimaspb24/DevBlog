package com.project.devblog.controller;

import com.project.devblog.controller.dto.request.AuthenticationRequest;
import com.project.devblog.model.UserEntity;
import com.project.devblog.service.AuthUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final AuthUserService authUserService;

    @PostMapping
    public ResponseEntity registration(@RequestBody AuthenticationRequest requestDto) {
        String login = requestDto.getLogin();
        String password = requestDto.getPassword();

        if (authUserService.findByLogin(login) == null && !login.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setLogin(login);
            user.setPassword(password);

            authUserService.create(user);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Login already exists");
        }
    }
}
