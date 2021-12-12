package com.project.devblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationResponseDto {

    private String login;
    private String token;
}
