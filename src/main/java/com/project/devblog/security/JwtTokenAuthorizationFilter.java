package com.project.devblog.security;

import com.project.devblog.exception.JwtAuthenticationException;
import static com.project.devblog.security.JwtTokenProvider.AUTH_HEADER_KEY;
import static com.project.devblog.security.JwtTokenProvider.TOKEN_PREFIX;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(AUTH_HEADER_KEY);

        if (Objects.isNull(bearerToken) || !bearerToken.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            validateToken(bearerToken);
            var authentication = jwtTokenProvider.getAuthentication(bearerToken);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
//            new CustomAuthenticationEntryPoint().commence(request, response, ex);
        }
        filterChain.doFilter(request, response);
    }

    private void validateToken(String bearerToken) {
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            try {
                jwtTokenProvider.validateToken(bearerToken);
            } catch (ExpiredJwtException e) {
                throw new JwtAuthenticationException("Expired token", e);
            } catch (UnsupportedJwtException | MalformedJwtException e) {
                throw new JwtAuthenticationException("Unsupported token", e);
            } catch (JwtException | IllegalArgumentException e) {
                throw new JwtAuthenticationException("JWT token is invalid", e);
            } catch (Exception e) {
                throw new JwtAuthenticationException("User authorization not resolved", e);
            }
        } else {
            throw new JwtAuthenticationException("Authorization token not found");
        }
    }
}
