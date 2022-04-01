package com.project.devblog.service;

import com.project.devblog.dto.response.AuthenticationResponse;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse login(@NonNull String login, @NonNull String password) {
        final UserEntity user;
        try {
            user = userService.findByLogin(login);
        } catch (NotFoundException ex) {
            throw new NotFoundException(format("The account with login %s does not exist", login));
        }

        if (!user.isEnabled()) {
            if (user.getVerificationCode() != null) {
                throw new VerificationException("This account is not verified");
            }
            throw new LockedException("This account is blocked");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        return new AuthenticationResponse(user.getId(), login, user.getRole().toString());
    }

    public AuthenticationResponse register(@NonNull String login, @NonNull String password) {
        if (!userService.isExists(login)) {
            UserEntity user = userService.register(login, password);
            return new AuthenticationResponse(user.getId(), login, user.getRole().toString());
        } else {
            throw new BadCredentialsException(format("The account with login %s is already registered", login));
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, auth);
    }
}
