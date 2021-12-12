package com.project.devblog.controller;

import com.project.devblog.dto.AuthenticationRequestDto;
import com.project.devblog.model.UserEntity;
import com.project.devblog.service.UserService;
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

    private final UserService userService;

    @PostMapping
    public ResponseEntity registration(@RequestBody AuthenticationRequestDto requestDto) {
        String login = requestDto.getLogin();
        String password = requestDto.getPassword();

        if (userService.findByLogin(login) == null && !login.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setLogin(login);
            user.setPassword(password);

            userService.register(user);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Login already exists");
        }
    }
}
