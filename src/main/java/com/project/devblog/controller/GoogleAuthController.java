package com.project.devblog.controller;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import com.project.devblog.security.jwt.JwtTokenProvider;
import com.project.devblog.service.UserService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@AllArgsConstructor
public class GoogleAuthController {

    @NonNull
    private final UserService userService;
    @NonNull
    private final JwtTokenProvider jwtTokenProvider;
    private final String URL = "http://%s:%s/v1/users/%s";

    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    @GetMapping("/")
    public RedirectView login(@NonNull Authentication authentication, @NonNull HttpServletResponse httpServletResponse) {
        final DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
        final UserEntity userEntity = userService.createUser(user.getSubject(), user.getEmail(), Role.USER, StatusUser.ACTIVE, user.getGivenName(),
                user.getFamilyName(), user.getFullName(), user.getPicture(), user.getPhoneNumber());

        httpServletResponse.addCookie(new Cookie("access_token", "Bearer_" + jwtTokenProvider.createToken(userEntity.getLogin(), Role.USER)));
        return new RedirectView(String.format(URL, "localhost", "8080", userEntity.getId()));
    }
}
