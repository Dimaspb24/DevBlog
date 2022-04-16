package com.project.devblog.security;

import com.project.devblog.model.UserEntity;
import com.project.devblog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) {
        UserEntity user = userService.findByLogin(login);
        return JwtUserFactory.create(user);
        // User.withUsername(username).password(applicationUser.getPassword()).roles("USER").disabled(!applicationUser.isVerified()).build();
    }
}
