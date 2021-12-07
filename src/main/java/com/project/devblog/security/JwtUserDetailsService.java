package com.project.devblog.security;

import com.project.devblog.model.UserEntity;
import com.project.devblog.security.jwt.JwtUserFactory;
import com.project.devblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = userService.findByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException("User with login: " + login + " NOT FOUND!");
        }

        return JwtUserFactory.create(user);
    }
}
