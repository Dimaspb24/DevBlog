package com.project.devblog.security.jwt;

import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class JwtUserFactory {

    public static JwtUser create(UserEntity user) {
        return new JwtUser(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getStatus().equals(StatusUser.ACTIVE),
                mapToGrantedAuthorities()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities() {
        return new ArrayList<Role>(Arrays.asList(Role.values())).stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.name())
                ).collect(Collectors.toList());
    }
}
