package com.project.devblog.handler;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.project.devblog.security.JwtTokenProvider.AUTH_HEADER_KEY;
import static com.project.devblog.security.JwtTokenProvider.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse
            response, Authentication authentication) throws IOException, ServletException {

        final DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
        UserEntity userEntity = userRepository.findById(user.getSubject()).orElseGet(() ->
                userService.create(user.getSubject(), user.getEmail(), Role.USER,
                        true, user.getGivenName(), user.getFamilyName(), user.getFullName(),
                        user.getPicture(), user.getPhoneNumber())
        );

        String bearerToken = TOKEN_PREFIX + jwtTokenProvider.createToken(userEntity.getLogin(), Role.USER);
        response.addHeader(AUTH_HEADER_KEY, bearerToken); // токен не передаётся, поэтому на USERs не заходит

        redirectStrategy.sendRedirect(request, response, "/v1/articles");
    }
}
