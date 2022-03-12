package com.project.devblog.controller.authcontroller;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenProvider;
import static com.project.devblog.security.JwtTokenProvider.AUTH_HEADER_KEY;
import static com.project.devblog.security.JwtTokenProvider.TOKEN_PREFIX;
import com.project.devblog.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "Google authentication")
@RestController
@RequiredArgsConstructor
public class GoogleAuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.google.redirect.url}")
    private String url;

    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    @GetMapping("/")
    public RedirectView login(@NonNull Authentication authentication, @NonNull HttpServletResponse httpServletResponse) {
        final DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
        final UserEntity userEntity = userService.create(user.getSubject(), user.getEmail(), Role.USER, true, user.getGivenName(),
                user.getFamilyName(), user.getFullName(), user.getPicture(), user.getPhoneNumber());

        httpServletResponse.addHeader(AUTH_HEADER_KEY, TOKEN_PREFIX + jwtTokenProvider.createToken(userEntity.getLogin(), Role.USER));
        return new RedirectView(String.format(url, userEntity.getId()));
    }
}
