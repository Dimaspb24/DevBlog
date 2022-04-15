package com.project.devblog.security;

import com.project.devblog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) {
//        UserEntity user = userService.findByLogin(login);
//        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
//        return new User(user.getLogin(), user.getPassword(), Arrays.asList(authority));
        return UserPrincipal.create(userService.findByLogin(login));
    }

    public UserDetails loadUserById(String id) {
        return UserPrincipal.create(userService.findById(id));
    }
}
