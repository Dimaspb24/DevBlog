package com.project.devblog.security.jwt;

import com.project.devblog.config.JwtProperties;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.jwt.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtProperties jwtProperties;

    private String secret;
    private long validityInMs;

    @PostConstruct
    protected void init() {
        secret = jwtProperties.getSecret();
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        validityInMs = jwtProperties.getExpired();
    }

    public String createToken(String login, Role role) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("roles", getRoleNames(role));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token has expired or is invalid");
        }
    }

    private List<String> getRoleNames(Role role) {
        List<String> roleList = new ArrayList<>();
        roleList.add(role.nameWithPrefix());
        return roleList;
    }
}
